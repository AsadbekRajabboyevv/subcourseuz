package uz.asadbek.subcourse.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.asadbek.subcourse.auth.CustomUserDetails;
import uz.asadbek.subcourse.user.UserEntity;
import uz.asadbek.subcourse.util.JwtUtil;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final Integer BEGIN_INDEX = 7;
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        var authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = authHeader.substring(BEGIN_INDEX);

        try {
            var claims = JwtUtil.parseToken(token);

            var username = claims.getSubject();
            var userId = claims.get("id", Long.class);
            var roles = claims.get("roles", List.class);
            var lang = claims.get("lang", String.class);

            if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
                var authentication = getAuthenticationToken(
                    userId, username, lang, roles);

                authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                        .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            }

        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", ExceptionUtils.getStackTrace(e));
            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                "{\"success\":false,\"message\":\"Invalid or expired token\"}"
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken getAuthenticationToken(
        Long id, String username, String lang, List<String> roles) {

        var user = new UserEntity();
        user.setId(id);
        user.setEmail(username);
        user.setLanguage(lang);
        if (roles != null && !roles.isEmpty()) {
            user.setRole(roles.get(0));
        }

        var userDetails = CustomUserDetails.builder().user(user).build();

        return new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
    }
}
