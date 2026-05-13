package uz.asadbek.subcourse.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.ai.dto.TestGenerateRequestDto;

@RestController
@RequiredArgsConstructor
public class AiController implements AiApi {

    private final AiService service;
    @Override
    public BaseResponseDto<String> testGenerate(TestGenerateRequestDto request,
        MultipartFile mainImage,
        MultipartFile file) {
        service.testGenerate(file, mainImage, request);
        return BaseResponseDto.ok("success", "Test generated successfully.");
    }
}
