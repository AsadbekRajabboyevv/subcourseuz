package uz.asadbek.subcourse.ai;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.ai.dto.TestGenerateRequestDto;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/v1/api/ai")
public interface AiApi {

    @PostMapping("/test-generate")
    BaseResponseDto<String> testGenerate(
        @RequestPart @Valid TestGenerateRequestDto request,
        @RequestPart(required = false) MultipartFile mainImage,
        @RequestPart MultipartFile file
    );

    @Operation(summary = "Manual AI test generate", description = "Generates test from provided JSON data.")
    @PostMapping(value = "/manual/test-generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BaseResponseDto<String> manualTestGenerate(
        @RequestPart("request") @Valid TestGenerateRequestDto request,
        @RequestPart(value = "mainImage", required = false) MultipartFile mainImage
    );
}
