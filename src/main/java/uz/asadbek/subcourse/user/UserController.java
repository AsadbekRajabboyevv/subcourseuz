package uz.asadbek.subcourse.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.base.dto.BaseResponseDto;

import uz.asadbek.subcourse.user.dto.UserResponseDto;
import uz.asadbek.subcourse.user.filter.UserFilter;


@RestController
public class UserController implements UserApi {


    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Override
    public BaseResponseDto<UserResponseDto> getProfile() {
        return null;
    }

    @Override
    public BaseResponseDto<Long> countAllUsers() {
        return null;
    }

    @Override
    public BaseResponseDto<UserResponseDto> get(Long id) {
        return null;
    }

    @Override
    public BaseResponseDto<Page<UserResponseDto>> get(UserFilter filter, Pageable pageable) {
        return BaseResponseDto.ok(service.get(filter, pageable));
    }

    @Override
    public BaseResponseDto<UserResponseDto> create(UserResponseDto userResponseDto) {
        return null;
    }

    @Override
    public BaseResponseDto<Void> delete(Long id) {
        return null;
    }

    @Override
    public BaseResponseDto<UserResponseDto> update(Long id, UserResponseDto userResponseDto) {
        return null;
    }
}
