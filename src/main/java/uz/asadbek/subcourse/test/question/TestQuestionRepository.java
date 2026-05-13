package uz.asadbek.subcourse.test.question;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.test.question.dto.TestQuestionResponseDto;

@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestionEntity, Long> {

    @Query("""
              SELECT new uz.asadbek.subcourse.test.question.dto.TestQuestionResponseDto(
                 q.id,
                 q.text,
                 q.imagePath
              )
              from TestQuestionEntity q
              where q.testId = :testId
        """)
    List<TestQuestionResponseDto> getByTestId(Long testId);

    @Query("SELECT q FROM TestQuestionEntity q WHERE q.testId = :testId AND q.id IN (:ids)")
    List<TestQuestionEntity> findByIdsAndTestId(Set<Long> ids, Long testId);

    @Query(value = """
            SELECT *
            FROM test_questions
            WHERE test_id = :testId
            ORDER BY RANDOM()
            LIMIT :questionCount
        """, nativeQuery = true)
    List<TestQuestionEntity> findRandomQuestions(Long testId, Integer questionCount);

    List<TestQuestionEntity> findByTestId(Long id);

}
