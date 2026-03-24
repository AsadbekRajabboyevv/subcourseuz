package uz.asadbek.subcourse.auth;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.auth.dto.AuthRequestDto;
import uz.asadbek.subcourse.auth.dto.AuthResponseDto;
import uz.asadbek.subcourse.user.dto.UserRequestDto;

@RequestMapping("/v1/api/auth")
public interface AuthApi {

    @PostMapping("/login")
    BaseResponseDto<AuthResponseDto> login(@RequestBody @Valid AuthRequestDto dto, @RequestParam(required = false, defaultValue = "UZ") String language);

    @PostMapping("/register")
    BaseResponseDto<AuthResponseDto> register(@RequestBody @Valid UserRequestDto dto, @RequestParam(required = false, defaultValue = "UZ") String language);

    @GetMapping("/confirm")
    BaseResponseDto<Boolean> confirm(@RequestParam String confirmToken);
}
