package uz.asadbek.subcourse.ai;

import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.ai.dto.TestGenerateRequestDto;

public interface AiService {

    void testGenerate(MultipartFile file, MultipartFile mainImage, TestGenerateRequestDto request);

    void manualTestGenerate(String request, MultipartFile mainImage);
}
