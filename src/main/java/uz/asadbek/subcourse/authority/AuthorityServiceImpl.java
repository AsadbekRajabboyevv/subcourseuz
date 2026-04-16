package uz.asadbek.subcourse.authority;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.authority.dto.AuthorityRequestDto;
import uz.asadbek.subcourse.authority.dto.AuthorityResponseDto;
import uz.asadbek.subcourse.authority.filter.AuthorityFilter;
import uz.asadbek.subcourse.exception.BadRequestException;
import uz.asadbek.subcourse.exception.NotFoundException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository repository;
    private final AuthorityMapper mapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<AuthorityResponseDto>get(Pageable pageable, AuthorityFilter filter) {
        return repository.get(pageable, filter);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AuthorityResponseDto get(Long id) {
        var entity = findById(id);

        return mapper.toResponseDto(entity);
    }

    private AuthorityEntity findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> {
                log.warn("authority_not_found: {}", id);
                return new NotFoundException("authority_not_found");
            });
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long create(AuthorityRequestDto dto) {
        // Validate unique name
        check(repository.existsByName(dto.getName()), dto);

        var entity = new AuthorityEntity();
        entity.setName(dto.getName());

        repository.save(entity);
        return entity.getId();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long update(Long id, AuthorityRequestDto dto) {
        var entity = findById(id);

        check(repository.existsByNameAndIdNot(dto.getName(), id), dto);

        entity.setName(dto.getName());

        repository.save(entity);
        return entity.getId();
    }

    private void check(boolean repository, AuthorityRequestDto dto) {
        if (repository) {
            log.warn("authority_name_already_exists: {}", dto.getName());
            throw new BadRequestException("authority_name_already_exists");
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public Long delete(Long id) {
        var entity = findById(id);

        repository.delete(entity);
        return id;
    }
}
