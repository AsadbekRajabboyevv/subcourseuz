package uz.asadbek.subcourse.test.session.answer;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.asadbek.subcourse.exception.NotFoundException;
import uz.asadbek.subcourse.test.session.question.TestSessionQuestionRepository;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Service
@RequiredArgsConstructor
public class TestSessionAnswerServiceImpl implements TestSessionAnswerService {

    private final TestSessionAnswerRepository repository;
    private final TestSessionQuestionRepository testSessionQuestionRepository;

    @Override
    public void processAnswer(Long sessionId, Long questionId, Long optionId) {
        validate(sessionId, questionId);
        var sessionAnswer = repository.findBySessionIdAndQuestionId(
                sessionId, questionId)
            .orElse(new TestSessionAnswerEntity(sessionId, questionId, optionId));
        sessionAnswer.setSelectedOptionId(optionId);
        sessionAnswer.setAnsweredAt(LocalDateTime.now());
        repository.save(sessionAnswer);
    }

    @Override
    public List<TestSessionAnswerEntity> findBySessionId(Long sessionId) {
        return repository.findBySessionId(sessionId);
    }

    private void validate(Long sessionId, Long questionId) {
        var exists = testSessionQuestionRepository.existsBySessionIdAndQuestionId(sessionId,
            questionId);

        if (!exists) {
            throw ExceptionUtil.build(NotFoundException.class,
                "error.test_session_question.not_found");
        }
    }
}
