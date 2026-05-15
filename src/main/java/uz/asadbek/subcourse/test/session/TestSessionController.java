package uz.asadbek.subcourse.test.session;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.test.session.answer.dto.SubmitAnswerRequestDto;
import uz.asadbek.subcourse.test.session.dto.TestSessionResponseDto;
import uz.asadbek.subcourse.test.session.dto.UserTestSessionResponseDto;
import uz.asadbek.subcourse.test.test.dto.TestResultDto;
import uz.asadbek.subcourse.test.test.dto.TestReviewDto;

@RestController
@RequiredArgsConstructor
public class TestSessionController implements TestSessionApi {

    private final TestSessionService service;

    @Override
    public BaseResponseDto<Long> start(Long testId) {
        return BaseResponseDto.ok(service.startTestSession(testId));
    }

    @Override
    public BaseResponseDto<Boolean> submitAnswer(SubmitAnswerRequestDto request) {
        return BaseResponseDto.ok(service.submitAnswer(request) != null);
    }

    @Override
    public BaseResponseDto<TestResultDto> finish(Long sessionId) {
        return BaseResponseDto.ok(service.finishTestSession(sessionId, null));
    }

    @Override
    public BaseResponseDto<TestReviewDto> getReview(Long sessionId) {
        return BaseResponseDto.ok(service.getReview(sessionId));
    }

    @Override
    public BaseResponseDto<TestSessionResponseDto> getSession(Long sessionId) {
        return BaseResponseDto.ok(service.getSession(sessionId));
    }

    @Override
    public BaseResponseDto<Page<UserTestSessionResponseDto>> getSessions(Pageable pageable) {
        return BaseResponseDto.ok(service.getSessions(pageable));
    }
}
