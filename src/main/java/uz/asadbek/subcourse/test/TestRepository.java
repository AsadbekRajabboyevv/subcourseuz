package uz.asadbek.subcourse.test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.base.repository.BaseRepository;
import uz.asadbek.subcourse.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.filter.TestFilter;

import java.util.Optional;

@Repository
public interface TestRepository extends BaseRepository<TestEntity, Long> {

    Long countAllByDeletedAtIsNullAndIsPublishedIsTrue();

    @Modifying
    @Query("""
            UPDATE TestEntity t
            SET t.isPublished = false
            WHERE t.id = :id
        """)
    int unpublish(Long id);


    @Modifying
    @Query("""
            UPDATE TestEntity t
            SET t.isPublished = true
            WHERE t.id = :id
        """)
    int publish(Long id);

    @Query("""
            SELECT new uz.asadbek.subcourse.test.dto.TestResponseDto(
                t.id,
                t.name,
                t.description,
                t.price,
                t.lang,
                t.duration,
                t.isPublished,
                t.scienceId,
                t.courseId,
                t.lessonId,
                t.gradeId,
                t.imagePath,
                t.createdAt,
                t.updatedAt
            )
            FROM TestEntity t
            LEFT JOIN ScienceEntity s ON s.id = t.scienceId
            WHERE t.deletedAt IS NULL

            AND (:#{#filter.id} IS NULL OR t.id = :#{#filter.id})

            AND (:#{#filter.name} IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :#{#filter.name}, '%')))
            AND (:#{#filter.scienceName} IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :#{#filter.scienceName}, '%')))

            AND (:#{#filter.lang} IS NULL OR t.lang = :#{#filter.lang})

            AND (:#{#filter.durationFrom} IS NULL OR t.duration >= :#{#filter.durationFrom})
            AND (:#{#filter.durationTo} IS NULL OR t.duration <= :#{#filter.durationTo})

            AND (:#{#filter.priceFrom} IS NULL OR t.price >= :#{#filter.priceFrom})
            AND (:#{#filter.priceTo} IS NULL OR t.price <= :#{#filter.priceTo})

            AND (:#{#filter.gradeId} IS NULL OR t.gradeId = :#{#filter.gradeId})

            AND (:#{#filter.createdAtFrom} IS NULL OR t.createdAt >= :#{#filter.createdAtFrom})
            AND (:#{#filter.createdAtTo} IS NULL OR t.createdAt <= :#{#filter.createdAtTo})

            AND (:#{#filter.updatedAtFrom} IS NULL OR t.updatedAt >= :#{#filter.updatedAtFrom})
            AND (:#{#filter.updatedAtTo} IS NULL OR t.updatedAt <= :#{#filter.updatedAtTo})

            AND (:#{#filter.createdBy} IS NULL OR t.createdBy = :#{#filter.createdBy})
            AND (:#{#filter.updatedBy} IS NULL OR t.updatedBy = :#{#filter.updatedBy})
        """)
    Page<TestResponseDto> get(TestFilter filter, Pageable pageable);

    @Query("""
            SELECT new uz.asadbek.subcourse.test.dto.TestResponseDto(
                t.id,
                t.name,
                t.description,
                t.price,
                t.lang,
                t.duration,
                t.isPublished,
                t.scienceId,
                t.courseId,
                t.lessonId,
                t.gradeId,
                t.imagePath,
                t.createdAt,
                t.updatedAt
            )
            FROM TestEntity t
            LEFT JOIN ScienceEntity s ON s.id = t.scienceId
            WHERE t.deletedAt IS NULL
            AND t.id = :id
        """)
    Optional<TestResponseDto> get(Long id);
}
