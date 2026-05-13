package uz.asadbek.subcourse.comment;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.base.repository.BaseRepository;
import uz.asadbek.subcourse.comment.dto.CommentInfoResponseDto;
import uz.asadbek.subcourse.comment.dto.CommentResponseDto;
import uz.asadbek.subcourse.comment.filter.CommentFilter;

@Repository
public interface CommentRepository extends BaseRepository<CommentEntity, Long> {

    @Query("""
        SELECT new uz.asadbek.subcourse.comment.dto.CommentResponseDto(
            c.id,
            c.text,
            CONCAT(COALESCE(u.firstName, ''), ' ', COALESCE(u.lastName, '')),
            c.createdBy,
            c.createdAt,
            c.rating,
            c.courseSlug,
            c.lessonId,
            c.testId
        )
        FROM CommentEntity c
        LEFT JOIN UserEntity u ON u.id = c.createdBy
        WHERE c.deletedAt IS NULL
          AND (:#{#filter.courseSlug}   IS NULL OR c.courseSlug  = :#{#filter.courseSlug})
          AND (:#{#filter.lessonId}   IS NULL OR c.lessonId  = :#{#filter.lessonId})
          AND (:#{#filter.testId}     IS NULL OR c.testId    = :#{#filter.testId})
          AND (:#{#filter.ratingFrom} IS NULL OR c.rating   >= :#{#filter.ratingFrom})
          AND (:#{#filter.ratingTo}   IS NULL OR c.rating   <= :#{#filter.ratingTo})
          AND (:#{#filter.createdBy}  IS NULL OR c.createdBy = :#{#filter.createdBy})
          AND (:#{#filter.createdAtFrom} IS NULL OR c.createdAt >= :#{#filter.createdAtFrom})
          AND (:#{#filter.createdAtTo}   IS NULL OR c.createdAt <= :#{#filter.createdAtTo})
        ORDER BY c.createdAt DESC
        """)
    Page<CommentResponseDto> get(CommentFilter filter, Pageable pageable);

    @Query("""
        SELECT new uz.asadbek.subcourse.comment.dto.CommentInfoResponseDto(
            c.id,
            c.text,
            CONCAT(COALESCE(u.firstName, ''), ' ', COALESCE(u.lastName, '')),
            c.createdBy,
            c.createdAt,
            c.rating,
            c.courseSlug,
            c.lessonId,
            c.testId
        )
        FROM CommentEntity c
        LEFT JOIN UserEntity u ON u.id = c.createdBy
        WHERE c.deletedAt IS NULL AND c.id = :id
        """)
    CommentInfoResponseDto get(Long id);

    @Query("""
        SELECT AVG(CAST(c.rating AS double))
        FROM CommentEntity c
        WHERE c.deletedAt IS NULL
          AND (:courseSlug IS NULL OR c.courseSlug = :courseSlug)
          AND (:lessonId IS NULL OR c.lessonId = :lessonId)
          AND (:testId   IS NULL OR c.testId   = :testId)
        """)
    Double avgRating(String courseSlug, Long lessonId, Long testId);

    boolean existsByCreatedByAndCourseSlug(Long createdBy, String courseSlug);
    boolean existsByCreatedByAndLessonId(Long createdBy, Long lessonId);
    boolean existsByCreatedByAndTestId(Long createdBy, Long testId);

    @Query("""
            SELECT new uz.asadbek.subcourse.comment.dto.CommentResponseDto(
                c.id,
                c.text,
                CONCAT(COALESCE(u.firstName, ''), ' ', COALESCE(u.lastName, '')),
                c.createdBy,
                c.createdAt,
                c.rating,
                c.courseSlug,
                c.lessonId,
                c.testId
            )
            FROM CommentEntity c
            LEFT JOIN UserEntity u ON c.createdBy = u.id
            WHERE c.deletedAt IS NULL
            ORDER BY c.createdAt DESC
            LIMIT 4
            """)
    List<CommentResponseDto> getTop();
}
