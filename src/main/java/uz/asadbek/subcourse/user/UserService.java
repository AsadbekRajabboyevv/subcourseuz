package uz.asadbek.subcourse.user;

import java.util.Optional;
import uz.asadbek.base.service.BaseService;
import uz.asadbek.subcourse.user.dto.UserRequestDto;
import uz.asadbek.subcourse.user.dto.UserResponseDto;
import uz.asadbek.subcourse.user.filter.UserFilter;

public interface UserService extends
    BaseService<UserResponseDto, UserRequestDto, Long, UserFilter> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByConfirmationToken(String token);

    boolean emailExists(String email);

    UserEntity save(UserEntity user);

    UserEntity getCurrentUser();

    UserEntity findById(Long userId);

    Long count();
}
