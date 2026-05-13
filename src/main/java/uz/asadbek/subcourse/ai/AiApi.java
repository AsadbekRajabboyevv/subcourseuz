package uz.asadbek.subcourse.ai;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.ai.dto.TestGenerateRequestDto;

@RequestMapping("/v1/api/ai")
public interface AiApi {

    @PostMapping("/test-generate")
    BaseResponseDto<String> testGenerate(
        @RequestPart @Valid TestGenerateRequestDto request,
        @RequestPart(required = false) MultipartFile mainImage,
        @RequestPart MultipartFile file
    );
}
