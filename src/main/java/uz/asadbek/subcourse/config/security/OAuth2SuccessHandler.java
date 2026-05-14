package uz.asadbek.subcourse.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import uz.asadbek.subcourse.exception.BadRequestException;
import uz.asadbek.subcourse.user.UserService;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Value("${app.front.google.oauth2.success-url}")
    private String successUrl;

    @Override
    public void onAuthenticationSuccess(
        @NotNull HttpServletRequest request,
        HttpServletResponse response,
        @NotNull Authentication authentication
    ) throws IOException {

        var email = extractEmail(authentication);
        var user = userService.findByEmail(email).orElseThrow(()-> ExceptionUtil.build(
            BadRequestException.class, "auth.user_not_found", email));

        var token = JwtUtil.generateAccessToken(
            new CustomUserDetails(user, user.getId()));

        var redirectUrl = UriComponentsBuilder.fromUriString(successUrl)
            .queryParam("token", token)
            .toUriString();

        response.sendRedirect(redirectUrl);
    }

    private String extractEmail(Authentication authentication) {
        var principal = authentication.getPrincipal();
        if (principal instanceof UserDetails ud) {
            return ud.getUsername();
        }
        if (principal instanceof OAuth2User oauth2User) {
            return (String) oauth2User.getAttributes().get("email");
        }
        return authentication.getName();
    }
}
