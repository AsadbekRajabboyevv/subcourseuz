package uz.asadbek.subcourse.test;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.asadbek.subcourse.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.dto.TestResultDto;
import uz.asadbek.subcourse.test.dto.TestReviewDto;
import uz.asadbek.subcourse.test.filter.TestFilter;

public interface TestService {

    Long count();

    Long createTest(TestRequestDto testRequestDto);

    Page<TestResponseDto> getAllTest(TestFilter filter, Pageable pageable);

    TestResponseDto getTestById(Long id);

    void deleteTestById(Long id);

    TestResponseDto updateTest(Long id, TestRequestDto testRequestDto);

    Long startTest(Long testId);

    void finishTest(Long testId, Long sessionId);

    void submitAnswer(Long sessionId, Long questionId, Long optionId);

    TestResultDto getResult(Long sessionId);

    List<TestReviewDto> getReview(Long sessionId);
}
