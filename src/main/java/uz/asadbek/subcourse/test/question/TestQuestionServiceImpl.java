package uz.asadbek.subcourse.test.question;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.test.question.dto.TestQuestionResponseDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestQuestionServiceImpl implements TestQuestionService {

    private final TestQuestionRepository repository;

    @Override
    public List<TestQuestionEntity> getQuestions(Long testId, Set<Long> questionIds) {
        return repository.findByIdsAndTestId(questionIds, testId);
    }

    @Override
    public List<TestQuestionEntity> getRandomQuestions(Long testId, Integer count) {
        return repository.findRandomQuestions(testId, count);
    }

    @Override
    public List<TestQuestionResponseDto> getByTestId(Long testId) {
        return repository.getByTestId(testId);
    }

    @Override
    public List<TestQuestionEntity> findByTestId(Long id) {
        return repository.findByTestId(id);
    }

    @Override
    @Transactional
    public TestQuestionEntity save(TestQuestionEntity question) {
        return repository.save(question);
    }

    @Override
    @Transactional
    public void delete(TestQuestionEntity question) {
        repository.delete(question);
    }

    @Override
    public void deleteById(Long deleteQuestionId) {
        repository.deleteById(deleteQuestionId);
    }

    @Override
    public List<TestQuestionEntity> saveAll(List<TestQuestionEntity> questionsToSave) {
        return repository.saveAll(questionsToSave);
    }

}
