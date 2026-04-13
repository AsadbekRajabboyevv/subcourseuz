package uz.asadbek.subcourse.test;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.test.dto.SubmitAnswerRequestDto;
import uz.asadbek.subcourse.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.dto.TestResultDto;
import uz.asadbek.subcourse.test.dto.TestReviewDto;
import uz.asadbek.subcourse.test.dto.TestUpdateRequestDto;
import uz.asadbek.subcourse.test.filter.TestFilter;
import uz.asadbek.subcourse.test.session.TestSessionService;

@RestController
@RequiredArgsConstructor
public class TestController implements TestApi {

    private final TestService service;
    private final TestSessionService testSessionService;

    @Override
    public BaseResponseDto<Long> create(TestRequestDto request, MultipartFile image) {
        return BaseResponseDto.ok(service.createTest(request, image));
    }

    @Override
    public BaseResponseDto<Page<TestResponseDto>> get(Pageable pageable, TestFilter filter) {
        return BaseResponseDto.ok(service.get(filter, pageable));
    }

    @Override
    public BaseResponseDto<TestResponseDto> get(Long id) {
        return BaseResponseDto.ok(service.get(id));
    }

    @Override
    public BaseResponseDto<Long> unpublish(Long id) {
        return BaseResponseDto.ok(service.unpublishTest(id));
    }

    @Override
    public BaseResponseDto<Long> publish(Long id) {
        return BaseResponseDto.ok(service.publish(id));
    }

    @Override
    public BaseResponseDto<Boolean> submitAnswer(SubmitAnswerRequestDto request) {
        return BaseResponseDto.ok(testSessionService.submitAnswer(request));
    }

    @Override
    public BaseResponseDto<Long> update(Long id, TestUpdateRequestDto request) {
        return BaseResponseDto.ok(service.updateTest(id, request));
    }

    @Override
    public BaseResponseDto<Long> start(Long id) {
        return BaseResponseDto.ok(testSessionService.startTestSession(id));
    }

    @Override
    public BaseResponseDto<TestResultDto> finish(Long id) {
        return BaseResponseDto.ok(testSessionService.finishTestSession(id));
    }

    @Override
    public BaseResponseDto<TestReviewDto> getReview(Long sessionId) {
        return BaseResponseDto.ok(testSessionService.getReview(sessionId));
    }
}
