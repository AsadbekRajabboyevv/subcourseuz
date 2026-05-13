package uz.asadbek.subcourse.test.session.question;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.test.session.question.dto.TestSessionQuestionResponseDto;

@Repository
public interface TestSessionQuestionRepository extends
    JpaRepository<TestSessionQuestionEntity, Long> {

    boolean existsBySessionIdAndQuestionId(Long sessionId, Long questionId);

    @Query("""
            SELECT new uz.asadbek.subcourse.test.session.question.dto.TestSessionQuestionResponseDto(
               q.questionId,
               q.orderIndex,
               q.questionText,
               q.imagePath,
               a.selectedOptionId,
               CASE
                 WHEN a.id IS NOT NULL THEN TRUE
                 ELSE FALSE
               END,
               q.correctOptionId
            )
            FROM TestSessionQuestionEntity q
            LEFT JOIN TestSessionAnswerEntity a
                     ON q.sessionId = a.sessionId
                     AND q.questionId = a.questionId
            WHERE q.sessionId = :sessionId
            ORDER BY q.orderIndex
        """)
    List<TestSessionQuestionResponseDto> findBySessionId(Long sessionId);
}
