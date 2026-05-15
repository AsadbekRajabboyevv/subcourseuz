package uz.asadbek.subcourse.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.asadbek.subcourse.auth.dto.AuthProviderEnum;
import uz.asadbek.subcourse.util.JwtUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
        @NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull FilterChain chain
    ) throws ServletException, IOException {

        var token = extractToken(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                var claims = JwtUtil.parse(token).getBody();
                var email = claims.getSubject();
                var provider = claims.get(JwtUtil.CLAIM_PROVIDER, String.class);

                UserDetails userDetails;

                if (provider.equals(AuthProviderEnum.GOOGLE.name())) {
                    userDetails = userDetailsService.loadGoogleUser(email);
                } else {
                    userDetails = userDetailsService.loadUserByUsername(email);
                }

                var auth = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception ignored) {

            }
        }

        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        var header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(JwtUtil.BEARER_PREFIX)) {
            return header.substring(JwtUtil.BEARER_PREFIX.length());
        }
        return null;
    }
}
