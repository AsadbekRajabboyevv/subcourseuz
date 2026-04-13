package uz.asadbek.subcourse.test;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.test.dto.SubmitAnswerRequestDto;
import uz.asadbek.subcourse.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.dto.TestResultDto;
import uz.asadbek.subcourse.test.dto.TestReviewDto;
import uz.asadbek.subcourse.test.dto.TestUpdateRequestDto;
import uz.asadbek.subcourse.test.filter.TestFilter;


@RequestMapping("/v1/api/tests")
public interface TestApi {

    @PostMapping
    BaseResponseDto<Long> create(
        @RequestPart @Valid TestRequestDto request,
        @RequestPart MultipartFile image
    );

    @GetMapping
    BaseResponseDto<Page<TestResponseDto>> get(Pageable pageable, TestFilter filter);

    @GetMapping("/{id}")
    BaseResponseDto<TestResponseDto> get(@PathVariable Long id);

    @PutMapping("/unpublish/{id}")
    BaseResponseDto<Long> unpublish(@PathVariable Long id);

    @PutMapping("/publish/{id}")
    BaseResponseDto<Long> publish(@PathVariable Long id);

    @PutMapping("/submit")
    BaseResponseDto<Boolean> submitAnswer(@RequestBody @Valid SubmitAnswerRequestDto request);

    @PatchMapping("/{id}")
    BaseResponseDto<Long> update(@PathVariable Long id, TestUpdateRequestDto request);

    @PostMapping("/start/{id}")
    BaseResponseDto<Long> start(@PathVariable Long id);

    @PostMapping("/finish/{id}")
    BaseResponseDto<TestResultDto> finish(@PathVariable Long id);

    @GetMapping("/review/{sessionId}")
    BaseResponseDto<TestReviewDto> getReview(@PathVariable Long sessionId);

}
