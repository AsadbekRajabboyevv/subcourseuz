package uz.asadbek.subcourse.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.auth.dto.AuthRequestDto;
import uz.asadbek.subcourse.auth.dto.AuthResponseDto;
import uz.asadbek.subcourse.user.dto.UserRequestDto;

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
    public BaseResponseDto<Boolean> confirm(String token) {
        return BaseResponseDto.ok(authService.confirmUser(token));
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
