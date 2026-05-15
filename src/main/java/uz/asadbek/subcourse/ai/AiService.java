package uz.asadbek.subcourse.ai;

import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.ai.dto.TestGenerateRequestDto;

public interface AiService {

    void testGenerate(MultipartFile file, MultipartFile mainImage, TestGenerateRequestDto request);

    void manualTestGenerate(TestGenerateRequestDto request, MultipartFile mainImage);
}
