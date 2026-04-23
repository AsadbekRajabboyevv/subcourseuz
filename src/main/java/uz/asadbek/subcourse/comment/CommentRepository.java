package uz.asadbek.subcourse.comment;

import java.util.Optional;
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

    Optional<CommentEntity> findFirstByCreatedByOrderByCreatedAtDesc(Long createdBy);

    @Query("""
        SELECT new uz.asadbek.subcourse.comment.dto.CommentResponseDto(
            c.id, c.text,
            CONCAT(COALESCE(u.firstName,''), ' ', COALESCE(u.lastName,'')),
            c.createdBy, c.createdAt,
            c.rating, c.courseId, c.lessonId, c.testId
        )
        FROM CommentEntity c
        LEFT JOIN UserEntity u ON u.id = c.createdBy
        WHERE c.deletedAt IS NULL
          AND (:#{#filter.courseId} IS NULL OR c.courseId = :#{#filter.courseId})
          AND (:#{#filter.lessonId} IS NULL OR c.lessonId = :#{#filter.lessonId})
          AND (:#{#filter.testId}   IS NULL OR c.testId   = :#{#filter.testId})
          AND (:#{#filter.createdBy} IS NULL OR c.createdBy = :#{#filter.createdBy})
        ORDER BY c.createdAt DESC
        """)
    Page<CommentResponseDto> get(CommentFilter filter, Pageable pageable);

    @Query("""
        SELECT new uz.asadbek.subcourse.comment.dto.CommentInfoResponseDto(
            c.id, c.text,
            CONCAT(COALESCE(u.firstName,''), ' ', COALESCE(u.lastName,'')),
            c.createdBy, c.createdAt,
            c.rating, c.courseId, c.lessonId, c.testId
        )
        FROM CommentEntity c
        LEFT JOIN UserEntity u ON u.id = c.createdBy
        WHERE c.deletedAt IS NULL AND c.id = :id
        """)
    CommentInfoResponseDto get(Long id);

    @Query("""
        SELECT AVG(c.rating)
        FROM CommentEntity c
        WHERE c.deletedAt IS NULL
          AND (:courseId IS NULL OR c.courseId = :courseId)
          AND (:lessonId IS NULL OR c.lessonId = :lessonId)
        """)
    Double avgRating(Long courseId, Long lessonId);
}
