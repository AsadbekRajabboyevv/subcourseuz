package uz.asadbek.subcourse.authority;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.authority.dto.AuthorityResponseDto;
import uz.asadbek.subcourse.authority.filter.AuthorityFilter;

@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("""
           select new uz.asadbek.subcourse.authority.dto.AuthorityResponseDto(
               a.id,
               a.name
           )
           from AuthorityEntity a
           where (:#{#filter.id} is null or a.id = :#{#filter.id})
           and (:#{#filter.name} is null or lower(a.name) like lower(concat('%', :#{#filter.name}, '%')))
        """)
    Page<AuthorityResponseDto> get(Pageable pageable, AuthorityFilter filter);
}
