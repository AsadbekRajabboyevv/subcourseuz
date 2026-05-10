package uz.asadbek.subcourse.test.question;

import java.util.List;
import java.util.Set;
import uz.asadbek.subcourse.test.question.dto.TestQuestionResponseDto;

public interface TestQuestionService {

    List<TestQuestionEntity> getRandomQuestions(Long testId, Integer questionCount);

    List<TestQuestionEntity> getQuestions(Long testId, Set<Long> questionIds);

    List<TestQuestionResponseDto> getByTestId(Long testId);

    List<TestQuestionEntity> findByTestId(Long id);

    void save(TestQuestionEntity question);

    void delete(TestQuestionEntity question);

    void deleteAllByIds(List<Long> deleteQuestionIds);
}
