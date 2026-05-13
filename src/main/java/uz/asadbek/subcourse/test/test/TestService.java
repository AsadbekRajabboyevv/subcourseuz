package uz.asadbek.subcourse.test.test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.ai.dto.GeminiTestGenerateResponseDto;
import uz.asadbek.subcourse.test.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.test.dto.TestUpdateRequestDto;
import uz.asadbek.subcourse.test.test.filter.TestFilter;

public interface TestService {

    Long count();

    Page<TestResponseDto> get(TestFilter filter, Pageable pageable);

    TestResponseDto get(Long id);

    TestResponseDto getInfo(Long id);

    TestEntity getById(Long id);

    Long create(TestRequestDto testRequestDto, MultipartFile image, MultipartFile[] questionImages,
        MultipartFile[] optionImages);

    Long update(Long id, TestUpdateRequestDto request, MultipartFile image, MultipartFile[] qFiles,
        MultipartFile[] oFiles);

    Long publish(Long id);

    Long unpublish(Long id);

    void enroll(Long testId);

    void saveAiGeneratedTest(GeminiTestGenerateResponseDto request, Long testId);

    void save(TestEntity newTest);
}
