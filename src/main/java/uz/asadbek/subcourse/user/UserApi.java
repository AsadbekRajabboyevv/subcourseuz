package uz.asadbek.subcourse.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.user.dto.UserResponseDto;
import uz.asadbek.subcourse.user.filter.UserFilter;

@RequestMapping("/v1/api/users")
@Tag(name = "Userlar", description = "Userlar")
public interface UserApi {

    @GetMapping("/profile")
    BaseResponseDto<UserResponseDto> getProfile();

    @GetMapping("/count")
    BaseResponseDto<Long> countAllUsers();

    @GetMapping("/{id}")
    BaseResponseDto<UserResponseDto> get(Long id);

    @GetMapping
    BaseResponseDto<Page<UserResponseDto>> get(UserFilter filter, Pageable pageable);

    @PostMapping
    BaseResponseDto<UserResponseDto> create(UserResponseDto userResponseDto);

    @DeleteMapping("/{id}")
    BaseResponseDto<Void> delete(Long id);

    @PutMapping("/{id}")
    BaseResponseDto<UserResponseDto> update(Long id, UserResponseDto userResponseDto);

}
