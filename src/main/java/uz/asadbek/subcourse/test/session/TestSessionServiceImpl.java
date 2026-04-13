package uz.asadbek.subcourse.test.session;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.test.dto.SubmitAnswerRequestDto;
import uz.asadbek.subcourse.test.dto.TestResultDto;
import uz.asadbek.subcourse.test.dto.TestReviewDto;
import uz.asadbek.subcourse.test.session.dto.TestSessionResponseDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestSessionServiceImpl implements TestSessionService{

    @Override
    public Long startTestSession(Long testId) {
        return 0L;
    }

    @Override
    public Boolean submitAnswer(SubmitAnswerRequestDto request) {
        return true;
    }

    @Override
    public TestResultDto finishTestSession(Long sessionId) {
        return null;
    }

    @Override
    public TestReviewDto getReview(Long sessionId) {
        return null;
    }

    @Override
    public TestSessionResponseDto getSession(Long sessionId) {
        return null;
    }
}
