package uz.asadbek.subcourse.user;

import uz.asadbek.base.service.BaseService;
import uz.asadbek.subcourse.auth.dto.AuthRequestDto;
import uz.asadbek.subcourse.auth.dto.AuthResponseDto;
import uz.asadbek.subcourse.user.dto.UserRequestDto;
import uz.asadbek.subcourse.user.dto.UserResponseDto;
import uz.asadbek.subcourse.user.filter.UserFilter;

public interface UserService extends BaseService<UserResponseDto, UserRequestDto, Long, UserFilter> {

    boolean emailExists(String email);
    boolean confirmUser(String token);
    String getConfirmationToken();
    void changeEmail(String email);
    AuthResponseDto register(UserRequestDto userRequestDto, String language);
    AuthResponseDto login(AuthRequestDto authRequestDto, String language);
    Long count();

}
