package uz.asadbek.subcourse.user;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.base.repository.BaseRepository;
import uz.asadbek.subcourse.user.dto.UserResponseDto;
import uz.asadbek.subcourse.user.filter.UserFilter;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<UserEntity> findByConfirmationToken(String token);

    @Query("""
            select new uz.asadbek.subcourse.user.dto.UserResponseDto(
                u.id,
                u.email,
                u.role,
                u.firstName,
                u.lastName,
                str(u.position),
                u.phone,
                u.birthDate,
                u.bio
            )
            from UserEntity u
            left join UserEntity creator on creator.id = u.createdBy
            left join UserEntity updatetor on updatetor.id = u.updatedBy
            where u.deletedAt is null
            and (:#{#filter.id} is null or u.id = :#{#filter.id})
            and (:#{#filter.email} is null or lower(u.email) like lower(concat('%', :#{#filter.email}, '%')))
            and (:#{#filter.role} is null or u.role = :#{#filter.role})
            and (:#{#filter.firstName} is null or lower(u.firstName) like lower(concat('%', :#{#filter.firstName}, '%')))
            and (:#{#filter.lastName} is null or lower(u.lastName) like lower(concat('%', :#{#filter.lastName}, '%')))
            and (:#{#filter.phone} is null or u.phone like concat('%', :#{#filter.phone}, '%'))

            and (:#{#filter.createdAtFrom} is null or u.createdAt >= :#{#filter.createdAtFrom})
            and (:#{#filter.createdAtTo} is null or u.createdAt <= :#{#filter.createdAtTo})

            and (:#{#filter.updatedAtFrom} is null or u.updatedAt >= :#{#filter.updatedAtFrom})
            and (:#{#filter.updatedAtTo} is null or u.updatedAt <= :#{#filter.updatedAtTo})

            and (:#{#filter.createdBy} is null or u.createdBy = :#{#filter.createdBy})
            and (:#{#filter.updatedBy} is null or u.updatedBy = :#{#filter.updatedBy})
            and (:#{#filter.createdByName} is null or lower(concat(creator.firstName, ' ', creator.lastName)) like lower(concat('%', :#{#filter.createdByName}, '%')))
            and (:#{#filter.updatedByName} is null or lower(concat(updatetor.firstName, ' ', updatetor.lastName)) like lower(concat('%', :#{#filter.updatedByName}, '%')))
        """)
    Page<UserResponseDto> get(UserFilter filter, Pageable pageable);

    @Query("""
           select new uz.asadbek.subcourse.user.dto.UserResponseDto(
               u.id,
               u.email,
               u.role,
               u.firstName,
               u.lastName,
               str(u.position),
               u.phone,
               u.birthDate,
               u.bio
           )
           from UserEntity u
           where u.deletedAt is null
        """)
    UserResponseDto get(Long id);

    Optional<UserEntity> findByEmail(String email);
}
