package uz.asadbek.subcourse.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.asadbek.subcourse.auth.CustomUserDetails;
import uz.asadbek.subcourse.exception.InvalidTokenException;
import uz.asadbek.subcourse.exception.TokenExpiredException;
import uz.asadbek.subcourse.user.UserEntity;
import uz.asadbek.subcourse.util.JwtUtil;

/**
 * JWT Bearer-token authentication filter.
 *
 * <p>Intercepts every HTTP request exactly once (extends {@link OncePerRequestFilter}),
 * resolves the Bearer token from the {@code Authorization} header, validates it, and populates the
 * Spring {@link SecurityContextHolder} with a fully-authenticated token.
 *
 * <p><b>Flow:</b>
 * <pre>
 *  request
 *    │
 *    ├─ no / invalid header  ──► pass-through (anonymous)
 *    │
 *    ├─ valid JWT            ──► set Authentication → pass-through
 *    │
 *    └─ expired JWT          ──► 401 UNAUTHORIZED (JSON body)
 *    └─ invalid JWT          ──► 401 UNAUTHORIZED (JSON body)
 * </pre>
 *
 * <p><b>Thread-safety:</b> Stateless – one Spring singleton is safe for concurrent use.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    /**
     * Constructs a {@link CustomUserDetails} from JWT claim values.
     *
     * <p>Only the data embedded in the token is used – no database call is made,
     * which is the intentional stateless design. If full entity hydration is required (e.g.
     * account-locked checks), inject a {@code UserDetailsService} and load the user here.
     *
     * @param id       user primary key
     * @param username email / login name (JWT subject)
     * @param lang     preferred language code
     * @param roles    role names without the {@code ROLE_} prefix
     * @return fully constructed user details
     */
    private static CustomUserDetails buildUserDetails(
        Long id, String username, String lang, List<String> roles
    ) {
        var user = new UserEntity();
        user.setId(id);
        user.setEmail(username);
        user.setLanguage(lang);
        if (!roles.isEmpty()) {
            user.setRole(roles.get(0));
        }

        return CustomUserDetails.builder().user(user).build();
    }

    /**
     * Resolves the real client IP, respecting common reverse-proxy headers.
     *
     * @param request HTTP request
     * @return client IP string
     */
    private static String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwarded)) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        Optional<String> tokenOpt = JwtUtil.resolveAccessToken(request);

        if (tokenOpt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = tokenOpt.get();

        try {
            authenticateFromToken(token, request);
        } catch (TokenExpiredException ex) {
            log.warn("[JWT] Expired token – uri={} ip={}",
                request.getRequestURI(), getClientIp(request));
            writeErrorResponse(response, HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED",
                "Access token has expired. Please refresh your session.");
            return;
        } catch (InvalidTokenException ex) {
            log.warn("[JWT] Invalid token – uri={} ip={} reason={}",
                request.getRequestURI(), getClientIp(request), ex.getMessage());
            writeErrorResponse(response, HttpStatus.UNAUTHORIZED, "TOKEN_INVALID",
                "Access token is invalid.");
            return;
        } catch (Exception ex) {
            log.error("[JWT] Unexpected error during token processing – uri={}",
                request.getRequestURI(), ex);
            SecurityContextHolder.clearContext();
            writeErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_ERROR",
                "Authentication could not be completed.");
            return;
        }

        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/v1/api/auth/") ||
            path.startsWith("/v1/api/public/") ||
            path.startsWith("/swagger-ui/") ||
            path.startsWith("/v3/api-docs");
    }
    /**
     * Parses the token, builds a {@link CustomUserDetails} principal, and sets the authentication
     * in the {@link SecurityContextHolder}.
     *
     * <p>Skips population if authentication is already present (avoids
     * overwriting a previously set authentication in the same request).
     *
     * @param token   raw JWT string
     * @param request current HTTP request (used for details builder)
     */
    private void authenticateFromToken(String token, HttpServletRequest request) {
        var claims = JwtUtil.parseToken(token);

        String username = claims.getSubject();
        if (!StringUtils.hasText(username)) {
            throw new InvalidTokenException("JWT subject (username) is missing");
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.trace("[JWT] Security context already populated – skipping for user '{}'",
                username);
            return;
        }

        Long userId = claims.get("id", Long.class);
        String lang = claims.get("lang", String.class);

        @SuppressWarnings("unchecked")
        List<String> roles = Optional
            .ofNullable(claims.get("roles", List.class))
            .orElse(Collections.emptyList());

        CustomUserDetails userDetails = buildUserDetails(userId, username, lang, roles);

        var authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("[JWT] Authenticated user='{}' roles={}", username, roles);
    }

    /**
     * Writes a structured JSON error body and sets the HTTP status.
     *
     * <p>Example response body:
     * <pre>
     * {
     *   "success":   false,
     *   "status":    401,
     *   "errorCode": "TOKEN_EXPIRED",
     *   "message":   "Access token has expired. Please refresh your session.",
     *   "timestamp": "2025-04-20T10:30:00Z"
     * }
     * </pre>
     *
     * @param response  HTTP response to write to
     * @param status    HTTP status to set
     * @param errorCode machine-readable code for client handling
     * @param message   human-readable description
     */
    private void writeErrorResponse(
        HttpServletResponse response,
        HttpStatus status,
        String errorCode,
        String message
    ) throws IOException {
        SecurityContextHolder.clearContext();

        var body = new ErrorResponse(
            false,
            status.value(),
            errorCode,
            message,
            Instant.now().toString()
        );

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), body);
    }

    /**
     * Immutable JSON payload returned on authentication failures.
     *
     * <p> Declared as a private record to keep the contract local to this filter.
     * If a shared error response format is introduced project-wide, replace with the common DTO.
     */
    private record ErrorResponse(
        boolean success,
        int status,
        String errorCode,
        String message,
        String timestamp
    ) {

    }
}
