package uz.asadbek.subcourse.test.session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.exception.BadRequestException;
import uz.asadbek.subcourse.exception.NotFoundException;
import uz.asadbek.subcourse.exception.UnAuthorizedException;
import uz.asadbek.subcourse.test.question.TestQuestionService;
import uz.asadbek.subcourse.test.session.question.TestSessionQuestionService;
import uz.asadbek.subcourse.test.test.TestService;
import uz.asadbek.subcourse.test.session.answer.dto.SubmitAnswerRequestDto;
import uz.asadbek.subcourse.test.test.dto.TestResultDto;
import uz.asadbek.subcourse.test.test.dto.TestReviewDto;
import uz.asadbek.subcourse.test.session.answer.TestSessionAnswerEntity;
import uz.asadbek.subcourse.test.session.answer.TestSessionAnswerService;
import uz.asadbek.subcourse.test.session.dto.TestSessionResponseDto;
import uz.asadbek.subcourse.test.session.dto.TestSessionStatus;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestSessionServiceImpl implements TestSessionService {

    private final TestSessionRepository repository;
    private final TestService testService;
    private final TestQuestionService questionService;
    private final TestSessionAnswerService sessionAnswerService;
    private final TestSessionQuestionService sessionQuestionService;

    @Override
    @Transactional
    public Long startTestSession(Long testId) {

        if (!JwtUtil.isAuthenticated()) {
            throw ExceptionUtil.build(UnAuthorizedException.class, "error.auth.user_not_authenticated");
        }

        var currentUserId = JwtUtil.getCurrentUserId();
        var now = LocalDateTime.now();
        if (repository.existsByUserIdAndTestIdAndStatus(currentUserId, testId, TestSessionStatus.STARTED)) {
            throw ExceptionUtil.build(BadRequestException.class, "error.test_session.already_started");
        }

        var test = testService.get(testId);
        var randomQuestions = questionService.getRandomQuestions(testId, test.getCount());
        if (randomQuestions.size() < test.getCount()) {
            throw ExceptionUtil.build(BadRequestException.class, "error.test.not_enough_questions");
        }

        var entity = new TestSessionEntity();
        entity.setUserId(currentUserId);
        entity.setTestId(testId);
        entity.setExpiresAt(now.plusMinutes(test.getDuration()));
        entity.setStatus(TestSessionStatus.STARTED);
        entity.setStartedAt(now);
        var sessionId = repository.save(entity).getId();
        sessionQuestionService.initializeSessionQuestions(sessionId, randomQuestions);
        return sessionId;
    }

    @Override
    public TestResultDto submitAnswer(SubmitAnswerRequestDto request) {
        var sessionId = request.getSessionId();
        var optionId = request.getOptionId();
        var questionId = request.getQuestionId();
        var entity = findById(sessionId);
        if (entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            return finishTestSession(sessionId, TestSessionStatus.TIME_OUT);
        }
        sessionAnswerService.processAnswer(sessionId, questionId, optionId);
        return null;
    }

    private TestSessionEntity findById(Long sessionId) {
        return repository.findById(sessionId)
            .orElseThrow(() -> ExceptionUtil.build(NotFoundException.class, "error.test_session.not_found"));
    }

    @Override
    public TestResultDto finishTestSession(Long sessionId, TestSessionStatus sessionStatus) {
        var entity = findById(sessionId);
        var score = calculateTestScore(entity);

        entity.setStatus(sessionStatus != null ? sessionStatus : TestSessionStatus.FINISHED);
        entity.setFinishedAt(LocalDateTime.now());
        entity.setScore(score.getScore());
        repository.save(entity);

        return score;
    }

    private TestResultDto calculateTestScore(TestSessionEntity entity) {
        var answerMap = sessionAnswerService
            .getAnswersBySessionId(entity.getId())
            .stream()
            .filter(a -> a.getSelectedOptionId() != null)
            .collect(Collectors.toMap(
                TestSessionAnswerEntity::getQuestionId,
                TestSessionAnswerEntity::getSelectedOptionId,
                (_, newVal) -> newVal
            ));

        var questions = questionService.getQuestions(entity.getTestId(), answerMap.keySet());
        var test = testService.getById(entity.getTestId());

        var correctAnswers = (int) questions.stream()
            .filter(question -> {
                Long selectedOptionId = answerMap.get(question.getId());
                return selectedOptionId != null
                    && selectedOptionId.equals(
                    question.getCorrectOptionId());
            }).count();

        var totalQuestions = test.getCount();
        var score = ((double) correctAnswers / totalQuestions) * test.getMaxScore();
        score = Math.round(score * 100.0) / 100.0;

        return new TestResultDto(score, correctAnswers, totalQuestions);
    }

    @Override
    public TestReviewDto getReview(Long sessionId) {
        return null;
    }

    @Override
    public TestSessionResponseDto getSession(Long sessionId) {
        var session = repository.findByIdAndTestInfo(sessionId);
        if (session.getFinishedAt() != null) {
            session.setRemainingSeconds(
                Duration.between(
                    session.getStartedAt(),
                    session.getFinishedAt()
                ).getSeconds());

        } else {
            session.setRemainingSeconds(
                Duration.between(
                    LocalDateTime.now(),
                    session.getExpiresAt()
                ).getSeconds());
        }
        session.setQuestions(sessionQuestionService.findBySessionId(sessionId));
        return session;
    }
}
