package uz.asadbek.subcourse.test.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.test.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.test.dto.TestUpdateRequestDto;
import uz.asadbek.subcourse.test.test.filter.TestFilter;


@RequestMapping("/v1/api/tests")
@Tag(
    name = "Tests",
    description = "APIs for managing tests, questions and options."
)
public interface TestApi {

    @Operation(summary = "Create test", description = "Creates a new test with questions, options and images.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BaseResponseDto<Long> create(

        @RequestPart(name = "request")
        @Valid @RequestBody(description = "Test creation payload")
        TestRequestDto request,

        @Parameter(description = "Main test image")
        @RequestPart(name = "mainImage", required = false)
        MultipartFile mainImage,

        @Parameter(description = "Question images")
        @RequestPart(name = "questionImages", required = false)
        MultipartFile[] questionImages,

        @Parameter(description = "Option images")
        @RequestPart(name = "optionImages", required = false)
        MultipartFile[] optionImages
    );

    @Operation(summary = "Get tests", description = "Returns paginated test list.")
    @GetMapping
    BaseResponseDto<Page<TestResponseDto>> get(
        @Parameter(hidden = true)
        Pageable pageable,
        @Parameter(hidden = true)
        TestFilter filter
    );

    @Operation(summary = "Get test details", description = "Returns detailed test information.")
    @GetMapping("/info/{id}")
    BaseResponseDto<TestResponseDto> getInfo(
        @Parameter(description = "Test ID")
        @PathVariable Long id
    );

    @Operation(summary = "Get test", description = "Returns basic test information.")
    @GetMapping("/{id}")
    BaseResponseDto<TestResponseDto> get(
        @Parameter(description = "Test ID")
        @PathVariable Long id
    );

    @Operation(summary = "Publish test", description = "Publishes test for users.")
    @PutMapping("/publish/{id}")
    BaseResponseDto<Long> publish(
        @Parameter(description = "Test ID")
        @PathVariable Long id
    );

    @Operation(summary = "Unpublish test", description = "Hides test from users.")
    @PutMapping("/unpublish/{id}")
    BaseResponseDto<Long> unpublish(
        @Parameter(description = "Test ID")
        @PathVariable Long id
    );

    @Operation(summary = "Update test", description = "Updates test data, questions and images.")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BaseResponseDto<Long> update(
        @Parameter(description = "Test ID")
        @PathVariable Long id,

        @RequestPart("request")
        TestUpdateRequestDto request,

        @Parameter(description = "Main test image")
        @RequestPart(required = false)
        MultipartFile image,

        @Parameter(description = "Question images")
        @RequestPart(required = false)
        MultipartFile[] questionImages,

        @Parameter(description = "Option images")
        @RequestPart(required = false)
        MultipartFile[] optionImages
    );
}
