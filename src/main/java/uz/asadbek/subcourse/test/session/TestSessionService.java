package uz.asadbek.subcourse.test.session;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.asadbek.subcourse.test.session.answer.dto.SubmitAnswerRequestDto;
import uz.asadbek.subcourse.test.session.dto.UserTestSessionResponseDto;
import uz.asadbek.subcourse.test.test.dto.TestResultDto;
import uz.asadbek.subcourse.test.test.dto.TestReviewDto;
import uz.asadbek.subcourse.test.session.dto.TestSessionResponseDto;
import uz.asadbek.subcourse.test.session.dto.TestSessionStatus;

public interface TestSessionService {

    Long startTestSession(Long testId);

    TestResultDto submitAnswer(SubmitAnswerRequestDto request);

    TestResultDto finishTestSession(Long sessionId, TestSessionStatus sessionStatus);

    TestReviewDto getReview(Long sessionId);

    TestSessionResponseDto getSession(Long sessionId);

    Page<UserTestSessionResponseDto> getSessions(Pageable pageable);
}
