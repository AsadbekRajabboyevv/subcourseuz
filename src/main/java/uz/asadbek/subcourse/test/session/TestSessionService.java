package uz.asadbek.subcourse.test.session;

import uz.asadbek.subcourse.test.dto.SubmitAnswerRequestDto;
import uz.asadbek.subcourse.test.dto.TestResultDto;
import uz.asadbek.subcourse.test.dto.TestReviewDto;
import uz.asadbek.subcourse.test.session.dto.TestSessionResponseDto;

public interface TestSessionService {

    Long startTestSession(Long testId);

    Boolean submitAnswer(SubmitAnswerRequestDto request);

    TestResultDto finishTestSession(Long sessionId);

    TestReviewDto getReview(Long sessionId);

    TestSessionResponseDto getSession(Long sessionId);
}
