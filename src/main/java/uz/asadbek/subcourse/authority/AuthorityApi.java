package uz.asadbek.subcourse.authority;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.authority.dto.AuthorityRequestDto;
import uz.asadbek.subcourse.authority.dto.AuthorityResponseDto;
import uz.asadbek.subcourse.authority.filter.AuthorityFilter;

@RequestMapping("/v1/api/authorities")
public interface AuthorityApi {

    @GetMapping
    BaseResponseDto<Page<AuthorityResponseDto>> get(
        AuthorityFilter filter,
        Pageable pageable
    );

    @GetMapping("/{id}")
    BaseResponseDto<AuthorityResponseDto> get(@PathVariable Long id);

    @PostMapping
    BaseResponseDto<Long> create(@Valid @RequestBody AuthorityRequestDto dto);

    @PutMapping("/{id}")
    BaseResponseDto<Long> update(
        @PathVariable Long id,
        @Valid @RequestBody AuthorityRequestDto dto
    );

    @DeleteMapping("/{id}")
    BaseResponseDto<Long> delete(@PathVariable Long id);
}
