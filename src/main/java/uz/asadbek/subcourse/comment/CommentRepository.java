package uz.asadbek.subcourse.comment;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.base.repository.BaseRepository;
import uz.asadbek.subcourse.comment.dto.CommentInfoResponseDto;

@Repository
public interface CommentRepository extends BaseRepository<CommentEntity, Long> {

    Optional<CommentEntity> findFirstByCreatedByOrderByCreatedAtDesc(Long createdBy);

    @Query("""
        SELECT new uz.asadbek.subcourse.comment.dto.CommentInfoResponseDto(
          c.id,
          c.text,
          COALESCE(creator.firstName , ' ' ,creator.lastName),
          c.createdBy,
          c.createdAt
        )
        FROM CommentEntity c
        JOIN UserEntity creator ON creator.id = c.createdBy
        WHERE c.deletedAt is null
        AND c.id = :id
    """)
    CommentInfoResponseDto get(Long id);
}
