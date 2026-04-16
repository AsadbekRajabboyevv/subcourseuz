package uz.asadbek.subcourse.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.asadbek.subcourse.auth.dto.AuthRequestDto;
import uz.asadbek.subcourse.auth.dto.AuthResponseDto;
import uz.asadbek.subcourse.user.dto.UserRequestDto;

public interface AuthService {

    AuthResponseDto login(AuthRequestDto authRequestDto, String language,
        HttpServletResponse response);

    AuthResponseDto register(UserRequestDto userRequestDto, String language);

    AuthResponseDto refresh(HttpServletRequest request,
        HttpServletResponse response);

    void logout(HttpServletRequest request, HttpServletResponse response);

    boolean confirmUser(String token);

    void changeEmail(String email);
}
