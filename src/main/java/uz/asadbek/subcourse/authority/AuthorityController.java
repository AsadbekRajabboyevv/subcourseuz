package uz.asadbek.subcourse.authority;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.authority.dto.AuthorityRequestDto;
import uz.asadbek.subcourse.authority.dto.AuthorityResponseDto;
import uz.asadbek.subcourse.authority.filter.AuthorityFilter;

@RestController
@RequiredArgsConstructor
public class AuthorityController implements AuthorityApi {

    private final AuthorityService authorityService;

    @Override
    public BaseResponseDto<Page<AuthorityResponseDto>> get(
        AuthorityFilter filter,
        Pageable pageable
    ) {
        return BaseResponseDto.ok(authorityService.get(pageable, filter));
    }

    @Override
    public BaseResponseDto<AuthorityResponseDto> get(Long id) {
        return BaseResponseDto.ok(authorityService.get(id));
    }

    @Override
    public BaseResponseDto<Long> create(AuthorityRequestDto dto) {
        return BaseResponseDto.ok(authorityService.create(dto));
    }

    @Override
    public BaseResponseDto<Long> update(Long id, AuthorityRequestDto dto) {
        return BaseResponseDto.ok(authorityService.update(id, dto));
    }

    @Override
    public BaseResponseDto<Long> delete(Long id) {
        return BaseResponseDto.ok(authorityService.delete(id));
    }
}
