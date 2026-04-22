package uz.asadbek.subcourse.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uz.asadbek.subcourse.auth.CustomUserDetails;
import uz.asadbek.subcourse.exception.InvalidTokenException;
import uz.asadbek.subcourse.exception.TokenExpiredException;

/**
 * Enterprise-grade JWT utility class.
 *
 * <p>Handles access token generation/validation, refresh token lifecycle,
 * confirmation token generation, cookie management, and security context access.
 *
 * <p><b>Thread-safety:</b> All methods are stateless or operate on volatile/static
 * fields initialized once at startup via {@link JwtUtilConfig}. Safe for concurrent use.
 */
@Slf4j
@UtilityClass
public class JwtUtil {

    private static final String BEARER_PREFIX           = "Bearer ";
    private static final String AUTHORIZATION_HEADER    = "Authorization";

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final String REFRESH_TOKEN_COOKIE_PATH = "/v1/api/auth";
    private static final int    REFRESH_TOKEN_COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7 days

    private static final String REFRESH_TOKEN_PREFIX      = "RFR_TKN_";
    private static final String CONFIRMATION_TOKEN_PREFIX = "CONF_TKN_";

    private static final int REFRESH_TOKEN_LENGTH      = 32;
    private static final int CONFIRMATION_TOKEN_LENGTH = 128;

    /**
     * Claim key names inside the JWT payload.
     *
     * <p>Declared {@code public} so that {@code JwtAuthenticationFilter} can reference
     * them directly, keeping token generation and claim extraction in sync through a
     * single source of truth. No other class should hard-code these strings.
     */
    public static final String CLAIM_ID       = "id";
    public static final String CLAIM_ROLES    = "roles";
    public static final String CLAIM_LANG     = "lang";
    public static final String CLAIM_USERNAME = "username";

    /** Spring-role prefix stripped when stored in the JWT. */
    private static final String ROLE_PREFIX = "ROLE_";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // -------------------------------------------------------------------------
    // Mutable state – set once at application startup via JwtUtilConfig
    // -------------------------------------------------------------------------

    public static volatile long   ACCESS_TOKEN_EXPIRATION_MS;
    private static volatile String secret;
    private static volatile Key    key;

    /**
     * Generates a signed JWT access token for the given user details.
     *
     * @param userDetails authenticated user
     * @return compact JWT string
     */
    public static String generateAccessToken(CustomUserDetails userDetails) {
        Objects.requireNonNull(userDetails, "userDetails must not be null");

        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(Objects::nonNull)
            .toList();

        Map<String, Object> claims = Map.of(
            CLAIM_ID,       userDetails.getId(),
            CLAIM_ROLES,    roles,
            CLAIM_LANG,     userDetails.getLanguage(),
            CLAIM_USERNAME, userDetails.getUsername()
        );

        String token = buildJwt(claims, userDetails.getUsername());
        log.debug("Access token generated for user '{}'", userDetails.getUsername());
        return token;
    }

    /**
     * Generates a secure opaque refresh token.
     *
     * @return prefixed, URL-safe Base64-encoded random token
     */
    public static String generateRefreshToken() {
        return generateOpaqueToken(REFRESH_TOKEN_PREFIX, REFRESH_TOKEN_LENGTH);
    }

    /**
     * Generates a secure opaque confirmation / email-verification token.
     *
     * @return prefixed, URL-safe Base64-encoded random token
     */
    public static String generateConfirmationToken() {
        return generateOpaqueToken(CONFIRMATION_TOKEN_PREFIX, CONFIRMATION_TOKEN_LENGTH);
    }

    // =========================================================================
    // Token Validation & Parsing
    // =========================================================================

    /**
     * Parses and validates a JWT access token.
     *
     * @param token raw JWT string
     * @return the token {@link Claims}
     * @throws TokenExpiredException  if the token has expired
     * @throws InvalidTokenException  if the token is malformed, unsigned incorrectly, etc.
     */
    public static Claims parseToken(String token) {
        Objects.requireNonNull(token, "token must not be null");
        try {
            return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException ex) {
            log.warn("JWT expired: {}", ex.getMessage());
            throw new TokenExpiredException("Access token has expired", ex);
        } catch (SignatureException ex) {
            log.warn("Invalid JWT signature: {}", ex.getMessage());
            throw new InvalidTokenException("Token signature is invalid", ex);
        } catch (MalformedJwtException ex) {
            log.warn("Malformed JWT: {}", ex.getMessage());
            throw new InvalidTokenException("Token is malformed", ex);
        } catch (UnsupportedJwtException ex) {
            log.warn("Unsupported JWT: {}", ex.getMessage());
            throw new InvalidTokenException("Token type is not supported", ex);
        } catch (JwtException ex) {
            log.warn("JWT processing failed: {}", ex.getMessage());
            throw new InvalidTokenException("Token processing failed", ex);
        }
    }

    /**
     * Checks whether a token string is a valid, non-expired JWT without throwing.
     *
     * @param token raw JWT string
     * @return {@code true} if valid and not expired
     */
    public static boolean isTokenValid(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        try {
            parseToken(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Returns the expiration time of a token as an {@link Instant}.
     *
     * @param token raw JWT string
     * @return expiration instant
     */
    public static Instant getTokenExpiry(String token) {
        return parseToken(token).getExpiration().toInstant();
    }

    /**
     * Extracts the subject (username) from a JWT without full validation.
     * Useful for logging; do NOT use for authorization decisions.
     *
     * @param token raw JWT string
     * @return subject claim value, or empty if unparseable
     */
    public static Optional<String> extractSubjectUnchecked(String token) {
        try {
            return Optional.ofNullable(parseToken(token).getSubject());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    /**
     * Resolves the Bearer access token from the {@code Authorization} header.
     *
     * @param request incoming HTTP request
     * @return token string, or empty if absent / malformed
     */
    public static Optional<String> resolveAccessToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            return Optional.of(header.substring(BEARER_PREFIX.length()).trim());
        }
        return Optional.empty();
    }

    /**
     * Writes an HTTP-only, Secure refresh-token cookie to the response.
     *
     * <p>Uses {@link ResponseCookie} for full attribute control (SameSite=Strict).
     *
     * @param response HTTP response
     * @param token    refresh token value
     */
    public static void setRefreshTokenCookie(HttpServletResponse response, String token) {
        Objects.requireNonNull(response, "response must not be null");
        Objects.requireNonNull(token, "token must not be null");

        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, token)
            .httpOnly(true)
            .secure(true)
            .path(REFRESH_TOKEN_COOKIE_PATH)
            .maxAge(REFRESH_TOKEN_COOKIE_MAX_AGE)
            .sameSite("Strict")
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
        log.debug("Refresh token cookie set");
    }

    /**
     * Clears the refresh-token cookie (sets MaxAge=0).
     *
     * @param response HTTP response
     */
    public static void clearRefreshTokenCookie(HttpServletResponse response) {
        Objects.requireNonNull(response, "response must not be null");

        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
            .httpOnly(true)
            .secure(true)
            .path(REFRESH_TOKEN_COOKIE_PATH)
            .maxAge(0)
            .sameSite("Strict")
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
        log.debug("Refresh token cookie cleared");
    }

    /**
     * Extracts the refresh token value from the request cookies.
     *
     * @param request incoming HTTP request
     * @return token string, or empty if cookie is absent
     */
    public static Optional<String> extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
            .filter(c -> REFRESH_TOKEN_COOKIE_NAME.equals(c.getName()))
            .map(Cookie::getValue)
            .filter(StringUtils::hasText)
            .findFirst();
    }

    /**
     * Returns the currently authenticated user from the Spring Security context.
     *
     * @return {@link CustomUserDetails}, or empty if unauthenticated / anonymous
     */
    public static Optional<CustomUserDetails> getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
            || !authentication.isAuthenticated()
            || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }

        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return Optional.of(userDetails);
        }

        return Optional.empty();
    }

    /**
     * Returns the current user, or throws if unauthenticated.
     *
     * @return non-null {@link CustomUserDetails}
     * @throws IllegalStateException if no authenticated user is present
     */
    public static CustomUserDetails requireCurrentUser() {
        return getCurrentUser()
            .orElseThrow(() -> new IllegalStateException("No authenticated user in security context"));
    }

    /** @return {@code true} if a user is authenticated in the current request */
    public static boolean isAuthenticated() {
        return getCurrentUser().isPresent();
    }

    /** @return {@code true} if the current user has ROLE_ADMIN */
    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    /** @return {@code true} if the current user has ROLE_TEACHER */
    public static boolean isTeacher() {
        return hasRole("ROLE_TEACHER");
    }

    /** @return {@code true} if the current user has ROLE_STUDENT */
    public static boolean isStudent() {
        return hasRole("ROLE_STUDENT");
    }

    /**
     * Checks whether the current user has the given authority string.
     *
     * @param role full role name, e.g. {@code "ROLE_ADMIN"}
     * @return {@code true} if the authority is present
     */
    public static boolean hasRole(String role) {
        return getCurrentUser()
            .map(u -> u.getAuthorities().stream()
                .anyMatch(a -> role.equals(a.getAuthority())))
            .orElse(false);
    }

    /**
     * Returns all roles of the current user (without the {@code ROLE_} prefix).
     *
     * @return unmodifiable list of role names; empty if unauthenticated
     */
    public static List<String> getCurrentUserRoles() {
        return getCurrentUser()
            .map(u -> u.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith(ROLE_PREFIX))
                .map(a -> a.substring(ROLE_PREFIX.length()))
                .toList())
            .orElse(Collections.emptyList());
    }

    /**
     * Returns the preferred language of the current user.
     *
     * @return language code, or {@code "en"} as a safe default
     */
    public static String getLanguage() {
        return getCurrentUser()
            .map(CustomUserDetails::getLanguage)
            .orElse("en");
    }

    /**
     * Returns the ID of the currently authenticated user.
     *
     * @return user ID
     * @throws IllegalStateException if unauthenticated
     */
    public static Long getCurrentUserId() {
        return requireCurrentUser().getId();
    }

    private static String buildJwt(Map<String, Object> claims, String subject) {
        Instant now = Instant.now();
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusMillis(ACCESS_TOKEN_EXPIRATION_MS)))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private static String generateOpaqueToken(String prefix, int byteLength) {
        byte[] bytes = new byte[byteLength];
        SECURE_RANDOM.nextBytes(bytes);
        return prefix + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Inner Spring {@link Component} that injects {@code @Value} properties
     * into the enclosing {@link UtilityClass} static fields at startup.
     *
     * <p>This pattern is required because {@code @UtilityClass} prevents
     * instantiation, so Spring cannot inject values directly.
     */
    @Component
    public static class JwtUtilConfig {

        @Value("${spring.security.custom.jwt-expiration-in-ms}")
        public void setAccessTokenExpirationMs(long value) {
            JwtUtil.ACCESS_TOKEN_EXPIRATION_MS = value;
            log.info("JWT access token expiration set to {} ms", value);
        }

        @Value("${spring.security.custom.jwt-secret}")
        public void setSecret(String value) {
            if (!StringUtils.hasText(value) || value.length() < 32) {
                throw new IllegalArgumentException(
                    "JWT secret must be at least 32 characters long");
            }
            JwtUtil.secret = value;
            JwtUtil.key = Keys.hmacShaKeyFor(value.getBytes(StandardCharsets.UTF_8));
            log.info("JWT signing key initialized");
        }
    }
}
