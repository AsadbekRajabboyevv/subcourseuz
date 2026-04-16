package uz.asadbek.subcourse.test.question;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.test.question.dto.TestQuestionResponseDto;

@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestionEntity, Long> {

    List<TestQuestionEntity> findByTestId(Long id);

    @Query("""
          SELECT new uz.asadbek.subcourse.test.question.dto.TestQuestionResponseDto(
             q.id,
             q.text,
             q.imagePath
          )
          from TestQuestionEntity q
    """)
    List<TestQuestionResponseDto> getByTestId(Long testId);
}
