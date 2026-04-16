package uz.asadbek.subcourse.test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.dto.TestUpdateRequestDto;
import uz.asadbek.subcourse.test.filter.TestFilter;

public interface TestService {

    Long count();

    Long createTest(TestRequestDto testRequestDto, MultipartFile image);

    Page<TestResponseDto> get(TestFilter filter, Pageable pageable);

    TestResponseDto get(Long id);

    Long unpublishTest(Long id);

    Long updateTest(Long id, TestUpdateRequestDto request);

    void enroll(Long testId);

    Long publish(Long id);
}
