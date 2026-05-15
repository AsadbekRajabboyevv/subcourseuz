package uz.asadbek.subcourse.test.session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.exception.BadRequestException;
import uz.asadbek.subcourse.exception.NotFoundException;
import uz.asadbek.subcourse.exception.UnAuthorizedException;
import uz.asadbek.subcourse.test.question.TestQuestionService;
import uz.asadbek.subcourse.test.session.dto.UserTestSessionResponseDto;
import uz.asadbek.subcourse.test.session.option.TestSessionOptionService;
import uz.asadbek.subcourse.test.session.question.TestSessionQuestionService;
import uz.asadbek.subcourse.test.test.TestService;
import uz.asadbek.subcourse.test.session.answer.dto.SubmitAnswerRequestDto;
import uz.asadbek.subcourse.test.test.dto.TestResultDto;
import uz.asadbek.subcourse.test.test.dto.TestReviewDto;
import uz.asadbek.subcourse.test.session.answer.TestSessionAnswerEntity;
import uz.asadbek.subcourse.test.session.answer.TestSessionAnswerService;
import uz.asadbek.subcourse.test.session.dto.TestSessionResponseDto;
import uz.asadbek.subcourse.test.session.dto.TestSessionStatus;
import uz.asadbek.subcourse.test.test.dto.TestReviewOptionDto;
import uz.asadbek.subcourse.test.test.dto.TestReviewQuestionDto;
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
    private final TestSessionOptionService testSessionOptionService;

    @Override
    @Transactional
    public Long startTestSession(Long testId) {
        if (!JwtUtil.isAuthenticated()) {
            throw ExceptionUtil.build(UnAuthorizedException.class,
                "error.auth.user_not_authenticated");
        }

        var currentUserId = JwtUtil.getCurrentUserId();
        var now = LocalDateTime.now();
        var idOpt = repository.findIdByUserIdAndTestIdAndStatus(currentUserId, testId,
            TestSessionStatus.STARTED);

        if (idOpt.isPresent()) {
            return idOpt.get();
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
        entity.setMaxScore(test.getMaxScore());
        var sessionId = repository.save(entity).getId();
        sessionQuestionService.initializeSessionQuestions(sessionId, randomQuestions);
        return sessionId;
    }

    @Override
    @Transactional
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
            .orElseThrow(
                () -> ExceptionUtil.build(NotFoundException.class, "error.test_session.not_found"));
    }

    @Override
    @Transactional
    public TestResultDto finishTestSession(Long sessionId, TestSessionStatus sessionStatus) {
        var entity = findById(sessionId);

        entity.setFinishedAt(LocalDateTime.now());
        entity.setStatus(sessionStatus != null ? sessionStatus : TestSessionStatus.FINISHED);

        var scoreDto = calculateTestScore(entity);

        entity.setScore(scoreDto.getScore());
        repository.save(entity);

        return scoreDto;
    }

    private TestResultDto calculateTestScore(TestSessionEntity session) {
        long durationInSeconds = java.time.Duration.between(session.getStartedAt(),
            session.getFinishedAt()).getSeconds();
        String spentTime = String.format("%02d:%02d", (durationInSeconds / 60),
            (durationInSeconds % 60));

        var answers = sessionAnswerService.findBySessionId(session.getId());

        var sessionQuestions = sessionQuestionService.findBySessionId(session.getId());
        int totalQuestions = sessionQuestions.size();

        Map<Long, Long> answerMap = answers.stream()
            .filter(a -> a.getSelectedOptionId() != null)
            .collect(Collectors.toMap(TestSessionAnswerEntity::getQuestionId,
                TestSessionAnswerEntity::getSelectedOptionId));

        long correctAnswers = sessionQuestions.stream()
            .filter(q -> {
                Long selected = answerMap.get(q.getId());
                return selected != null && selected.equals(q.getCorrectOptionId());
            }).count();

        double maxScore = session.getMaxScore();
        double score =
            (totalQuestions > 0) ? ((double) correctAnswers / totalQuestions) * maxScore : 0;

        score = Math.round(score * 100.0) / 100.0;

        return new TestResultDto(score, (int) correctAnswers, totalQuestions, session.getId(),
            spentTime,
            session.getStartedAt(), session.getFinishedAt());
    }

    @Override
    @Transactional(readOnly = true)
    public TestReviewDto getReview(Long sessionId) {
        var session = repository.findByIdAndTestInfo(sessionId);
        var sessionQuestions = sessionQuestionService.findBySessionId(sessionId);
        var userAnswers = sessionAnswerService.findBySessionId(sessionId);
        long durationInSeconds = 0;
        if (session.getFinishedAt() != null && session.getStartedAt() != null) {
            durationInSeconds = java.time.Duration.between(
                session.getStartedAt(),
                session.getFinishedAt()
            ).getSeconds();
        }

        var spentTime = String.format("%02d:%02d",
            (durationInSeconds % 3600) / 60,
            durationInSeconds % 60);
        Map<Long, Long> userAnswersMap = userAnswers.stream()
            .collect(Collectors.toMap(
                TestSessionAnswerEntity::getQuestionId,
                TestSessionAnswerEntity::getSelectedOptionId,
                (existing, _) -> existing
            ));

        int correctAnswersCount = 0;
        Boolean enabledViewCorrectAnswers = session.getEnabledViewCorrectAnswers();
        List<TestReviewQuestionDto> reviewQuestions = new ArrayList<>();

        for (var sQuestion : sessionQuestions) {
            boolean isCorrect = false;
            Integer selectedOrderNumber = null;
            Long selectedOptionId = userAnswersMap.get(sQuestion.getId());

            List<TestReviewOptionDto> reviewOptions = new ArrayList<>();
            var options = sQuestion.getOptions();

            if (options != null) {
                for (int i = 0; i < options.size(); i++) {
                    var opt = options.get(i);
                    int currentOrder = i + 1;


                    if (selectedOptionId != null && selectedOptionId.equals(opt.getId())) {
                        selectedOrderNumber = currentOrder;

                        if (opt.getId().equals(sQuestion.getCorrectOptionId())) {
                            isCorrect = true;
                        }
                    }

                    reviewOptions.add(TestReviewOptionDto.builder()
                        .id(opt.getId())
                        .text(opt.getText())
                        .imagePath(opt.getImagePath())
                        .build());
                }
            }

            if (isCorrect) {
                correctAnswersCount++;
            }

            reviewQuestions.add(TestReviewQuestionDto.builder()
                .questionText(sQuestion.getText())
                .imagePath(sQuestion.getImagePath())
                .selectedOptionOrderNumber(selectedOrderNumber)
                .correctOptionId(Boolean.TRUE.equals(enabledViewCorrectAnswers) ? sQuestion.getCorrectOptionId() : null)
                .isCorrect(isCorrect)
                .options(reviewOptions)
                .build());
        }

        int totalQuestions = sessionQuestions.size();
        double maxScore =
            session.getMaxScore() != null ? session.getMaxScore().doubleValue() : 100.0;

        double calculatedScore = 0.0;
        if (totalQuestions > 0) {
            calculatedScore = ((double) correctAnswersCount * maxScore) / totalQuestions;
        }

        double finalScore = Math.round(calculatedScore * 10.0) / 10.0;

        return TestReviewDto.builder()
            .testName(session.getTestName())
            .testAuthor(session.getAuthor())
            .testLang(session.getLang())
            .testScience(session.getScience())
            .testGrade(session.getGrade())
            .testImagePath(session.getImagePath())
            .testDescription(session.getTestDescription())
            .correctAnswers(correctAnswersCount)
            .enabledViewCorrectAnswers(session.getEnabledViewCorrectAnswers())
            .totalQuestions(totalQuestions)
            .questions(reviewQuestions)
            .score(finalScore)
            .maxScore(maxScore)
            .spentTime(spentTime)
            .startedAt(session.getStartedAt())
            .finishedAt(session.getFinishedAt())
            .build();
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

    @Override
    public Page<UserTestSessionResponseDto> getSessions(Pageable pageable) {
        return repository.findAllByUserId(JwtUtil.getCurrentUserId(), pageable);
    }
}
