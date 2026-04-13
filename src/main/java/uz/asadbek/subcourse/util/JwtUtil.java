package uz.asadbek.subcourse.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.asadbek.subcourse.auth.CustomUserDetails;

@Slf4j
@UtilityClass
public class JwtUtil {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final String REFRESH_TOKEN_COOKIE_PATH = "/v1/api/auth";
    private static final int REFRESH_TOKEN_COOKIE_MAX_AGE = 7 * 24 * 60 * 60;
    private static final String REFRESH_TOKEN_PREFIX = "RFR_TKN_";
    private static final String CONFIRMATION_TOKEN_PREFIX = "CONF_TKN_";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int REFRESH_TOKEN_LENGTH = 32;
    private static final int CONFIRMATION_TOKEN_LENGTH = 128;
    public static long ACCESS_TOKEN_EXPIRATION_MS;
    private static String secret;
    private static Key key;

    public static String generateRefreshToken() {
        return generateToken(REFRESH_TOKEN_PREFIX, REFRESH_TOKEN_LENGTH);
    }

    public static String generateConfirmationToken() {
        return generateToken(CONFIRMATION_TOKEN_PREFIX, CONFIRMATION_TOKEN_LENGTH);
    }

    public static void setRefreshTokenCookie(HttpServletResponse response, String token) {
        var cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(REFRESH_TOKEN_COOKIE_PATH);
        cookie.setMaxAge(REFRESH_TOKEN_COOKIE_MAX_AGE);
        response.addCookie(cookie);
    }

    public static void clearRefreshTokenCookie(HttpServletResponse response) {
        var cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(REFRESH_TOKEN_COOKIE_PATH);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
            .filter(c -> REFRESH_TOKEN_COOKIE_NAME.equals(c.getName())).map(Cookie::getValue)
            .findFirst().orElse(null);
    }

    public static CustomUserDetails getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails;
        }

        log.error("User is not authenticated");
        throw ExceptionUtil.badRequestException("user_not_authenticated");
    }

    public static boolean isAdmin() {
        return getCurrentUser().getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public static String getLanguage() {
        return getCurrentUser().getLanguage();
    }

    public static String generateAccessToken(CustomUserDetails userDetails) {

        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).filter(Objects::nonNull)
            .filter(a -> a.startsWith("ROLE_")).map(a -> a.substring(5)).toList();

        Map<String, Object> claims = Map.of("id", userDetails.getId(), "roles", roles, "lang",
            userDetails.getLanguage(), "username", userDetails.getUsername());

        return generateToken(claims, userDetails.getUsername());
    }

    private static String generateToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_MS);

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(now)
            .setExpiration(expiry).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private static String generateToken(String prefix, int tokenLength) {
        byte[] bytes = new byte[tokenLength];
        SECURE_RANDOM.nextBytes(bytes);

        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        return prefix + randomPart;
    }

    @Component
    public static class JwtUtilConfig {

        @Value("${spring.security.custom.jwt-expiration-in-ms}")
        public void setAccessTokenExpirationMs(long value) {
            JwtUtil.ACCESS_TOKEN_EXPIRATION_MS = value;
        }

        @Value("${spring.security.custom.jwt-secret}")
        public void setSecret(String value) {
            JwtUtil.secret = value;
            JwtUtil.key = Keys.hmacShaKeyFor(value.getBytes(StandardCharsets.UTF_8));
        }
    }
}
