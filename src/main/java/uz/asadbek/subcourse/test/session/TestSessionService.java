package uz.asadbek.subcourse.test.session;

import uz.asadbek.subcourse.test.dto.SubmitAnswerRequestDto;
import uz.asadbek.subcourse.test.dto.TestReviewDto;
import uz.asadbek.subcourse.test.session.dto.TestSessionResponseDto;

public interface TestSessionService {

    Long startTestSession(Long testId);

    void submitAnswer(SubmitAnswerRequestDto request);

    TestReviewDto finishTestSession(Long sessionId);

    TestReviewDto getReview(Long sessionId);

    TestSessionResponseDto getSession(Long sessionId);
}
