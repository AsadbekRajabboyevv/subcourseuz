package uz.asadbek.subcourse.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.auth.dto.AuthRequestDto;
import uz.asadbek.subcourse.auth.dto.AuthResponseDto;
import uz.asadbek.subcourse.user.UserService;
import uz.asadbek.subcourse.user.dto.UserRequestDto;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final UserService userService;

    @Override
    public BaseResponseDto<AuthResponseDto> login(AuthRequestDto dto, String language) {
        return BaseResponseDto.ok(userService.login(dto, language));
    }

    @Override
    public BaseResponseDto<AuthResponseDto> register(UserRequestDto dto, String language) {
        return BaseResponseDto.ok(userService.register(dto, language));
    }

    @Override
    public BaseResponseDto<Boolean> confirm(String token) {
        return BaseResponseDto.ok(userService.confirmUser(token));
    }
}
