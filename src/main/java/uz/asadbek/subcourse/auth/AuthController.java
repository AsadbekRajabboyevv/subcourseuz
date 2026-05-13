package uz.asadbek.subcourse.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.auth.dto.AuthRequestDto;
import uz.asadbek.subcourse.auth.dto.AuthResponseDto;
import uz.asadbek.subcourse.user.dto.UserRequestDto;
import uz.asadbek.subcourse.util.LangUtils;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public BaseResponseDto<AuthResponseDto> login(AuthRequestDto dto, String language,
        HttpServletResponse response) {
        return BaseResponseDto.ok(authService.login(dto, language, response));
    }

    @Override
    public BaseResponseDto<AuthResponseDto> register(UserRequestDto dto, String language) {
        return BaseResponseDto.ok(authService.register(dto, language));
    }

    @Override
    public void confirm(String token, HttpServletResponse response) {
        boolean success = authService.confirmUser(token);
        String baseUrl = "http://localhost:8080";
        String lang = LangUtils.currentLang();

        try {
            if (success) {
                response.sendRedirect("%s/%s/auth/confirm-success".formatted(baseUrl, lang));
            } else {
                response.sendRedirect("%s/%s/auth/confirm-error".formatted(baseUrl, lang));
            }
        } catch (IOException e) {
            log.error("Redirect failed for token {}: {}", token, ExceptionUtils.getStackTrace(e));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public BaseResponseDto<AuthResponseDto> refresh(HttpServletRequest request,
        HttpServletResponse response) {
        return BaseResponseDto.ok(authService.refresh(request, response));
    }

    @Override
    public BaseResponseDto<?> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return BaseResponseDto.ok(true);
    }

}
