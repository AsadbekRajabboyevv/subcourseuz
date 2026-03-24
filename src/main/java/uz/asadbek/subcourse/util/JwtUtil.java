package uz.asadbek.subcourse.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.asadbek.subcourse.auth.CustomUserDetails;
import uz.asadbek.subcourse.exception.BadRequestException;

@Slf4j
@Component
public class JwtUtil {

    @Value("${spring.security.custom.jwt-expiration-in-ms}")
    private long accessTokenExpirationMs;

    @Value("${spring.security.custom.jwt-secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(CustomUserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .filter(Objects::nonNull)
            .filter(a -> a.startsWith("ROLE_"))
            .map(a -> a.substring(5))
            .toList();

        log.info("roles: {}", roles);
        log.info("userDetails: {}", userDetails.toString());
        log.info("userDetails.getAuthorities(): {}", userDetails.getAuthorities());

        Map<String, Object> claims = Map.of(
            "role", roles,
            "lang", userDetails.getLanguage(),
            "username", userDetails.getUsername(),
            "id", userDetails.getId()
        );
        log.info("JWT userDetails: {}", userDetails);
        return generateToken(claims, userDetails.getUsername());
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpirationMs);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public static CustomUserDetails getCurrentUser() {
        Object principal = SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails;
        }

        throw ExceptionUtil.badRequestException("user_not_authenticated");
    }

    public long getAccessTokenExpirationMs() {
        return accessTokenExpirationMs;
    }
}
