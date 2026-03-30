package uz.asadbek.subcourse.test;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.asadbek.subcourse.test.dto.SubmitAnswerRequestDto;
import uz.asadbek.subcourse.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.dto.TestResultDto;
import uz.asadbek.subcourse.test.dto.TestReviewDto;
import uz.asadbek.subcourse.test.dto.TestUpdateRequestDto;
import uz.asadbek.subcourse.test.filter.TestFilter;

public interface TestService {

    Long count();

    Long createTest(TestRequestDto testRequestDto);

    Page<TestResponseDto> getAllTest(TestFilter filter, Pageable pageable);

    TestResponseDto get(Long id);

    Long unpublishTest(Long id);

    Long updateTest(Long id, TestUpdateRequestDto request);

    Long getPrice(Long testId);

    void enroll(Long userId, Long testId);

    void unenroll(Long userId, Long testId);

}
