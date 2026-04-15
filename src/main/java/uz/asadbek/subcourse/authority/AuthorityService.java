package uz.asadbek.subcourse.authority;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.asadbek.subcourse.authority.dto.AuthorityRequestDto;
import uz.asadbek.subcourse.authority.dto.AuthorityResponseDto;
import uz.asadbek.subcourse.authority.filter.AuthorityFilter;

public interface AuthorityService {

    Page<AuthorityResponseDto> get(Pageable pageable, AuthorityFilter filter);

    AuthorityResponseDto get(Long id);

    Long create(AuthorityRequestDto dto);

    Long update(Long id, AuthorityRequestDto dto);

    Long delete(Long id);
}
