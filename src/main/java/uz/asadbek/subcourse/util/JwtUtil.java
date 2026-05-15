package uz.asadbek.subcourse.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
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
import uz.asadbek.subcourse.config.security.CustomUserDetails;
import uz.asadbek.subcourse.exception.InvalidTokenException;
import uz.asadbek.subcourse.exception.TokenExpiredException;

@Slf4j
@UtilityClass
public class JwtUtil {

    public static final String CLAIM_ID = "id";
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_PROVIDER = "provider";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final String REFRESH_TOKEN_COOKIE_PATH = "/v1/api/auth";
    private static final int REFRESH_TOKEN_COOKIE_MAX_AGE = 7 * 24 * 60 * 60;
    private static final String REFRESH_TOKEN_PREFIX = "RFR_TKN_";
    private static final String CONFIRMATION_TOKEN_PREFIX = "CONF_TKN_";
    private static final int REFRESH_TOKEN_LENGTH = 32;
    private static final int CONFIRMATION_TOKEN_LENGTH = 128;
    private static final String ROLE_PREFIX = "ROLE_";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    public static volatile long ACCESS_TOKEN_EXPIRATION_MS;
    private static volatile String secret;
    private static volatile Key key;

    public static String generateAccessToken(CustomUserDetails userDetails) {
        Objects.requireNonNull(userDetails, "userDetails must not be null");

        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(Objects::nonNull)
            .toList();

        Map<String, Object> claims = Map.of(
            CLAIM_ID, userDetails.getId(),
            CLAIM_ROLES, roles,
            CLAIM_USERNAME, userDetails.getUsername(),
            CLAIM_PROVIDER, userDetails.getUser().getProvider()
        );

        return buildJwt(claims, userDetails.getUsername());
    }

    public static String generateRefreshToken() {
        return generateOpaqueToken(REFRESH_TOKEN_PREFIX, REFRESH_TOKEN_LENGTH);
    }

    public static String generateConfirmationToken() {
        return generateOpaqueToken(CONFIRMATION_TOKEN_PREFIX, CONFIRMATION_TOKEN_LENGTH);
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
    }


    public static Optional<String> resolveAccessToken(HttpServletRequest request) {
        var header = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            return Optional.of(header.substring(BEARER_PREFIX.length()).trim());
        }
        return Optional.empty();
    }

    public static void setRefreshTokenCookie(HttpServletResponse response, String token) {
        Objects.requireNonNull(response, "response must not be null");
        Objects.requireNonNull(token, "token must not be null");

        var cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, token)
            .httpOnly(true)
            .secure(true)
            .path(REFRESH_TOKEN_COOKIE_PATH)
            .maxAge(REFRESH_TOKEN_COOKIE_MAX_AGE)
            .sameSite("Strict")
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void clearRefreshTokenCookie(HttpServletResponse response) {
        Objects.requireNonNull(response, "response must not be null");

        var cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
            .httpOnly(true)
            .secure(true)
            .path(REFRESH_TOKEN_COOKIE_PATH)
            .maxAge(0)
            .sameSite("Strict")
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

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

    public static CustomUserDetails requireCurrentUser() {
        return getCurrentUser()
            .orElseThrow(
                () -> new IllegalStateException("No authenticated user in security context"));
    }

    public static boolean isAuthenticated() {
        return getCurrentUser().isPresent();
    }

    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public static boolean isStudent() {
        return hasRole("ROLE_STUDENT");
    }

    public static boolean hasRole(String role) {
        return getCurrentUser()
            .map(u -> u.getAuthorities().stream()
                .anyMatch(a -> role.equals(a.getAuthority())))
            .orElse(false);
    }

    public static List<String> getCurrentUserRoles() {
        return getCurrentUser()
            .map(u -> u.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith(ROLE_PREFIX))
                .map(a -> a.substring(ROLE_PREFIX.length()))
                .toList())
            .orElse(Collections.emptyList());
    }

    public static Long getCurrentUserId() {
        return requireCurrentUser().getId();
    }

    private static String buildJwt(Map<String, Object> claims, String subject) {
        var now = Instant.now();
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusMillis(ACCESS_TOKEN_EXPIRATION_MS)))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private static String generateOpaqueToken(String prefix, int byteLength) {
        var bytes = new byte[byteLength];
        SECURE_RANDOM.nextBytes(bytes);
        return prefix + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Component
    public static class JwtUtilConfig {

        @Value("${spring.security.custom.jwt-expiration-in-ms}")
        public void setAccessTokenExpirationMs(long value) {
                JwtUtil.ACCESS_TOKEN_EXPIRATION_MS = value;
        }

        @Value("${spring.security.custom.jwt-secret}")
        public void setSecret(String value) {
            if (!StringUtils.hasText(value) || value.length() < 32) {
                throw new IllegalArgumentException("JWT secret must be at least 32 characters long");
            }
            JwtUtil.secret = value;
            JwtUtil.key = Keys.hmacShaKeyFor(value.getBytes(StandardCharsets.UTF_8));
        }
    }
}
