package uz.asadbek.subcourse.test.question.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.test.option.dto.TestOptionRequestDto;

@Data
public class TestQuestionRequestDto {
    @NotNull
    private String text;
    private MultipartFile image;
    @NotNull
    private Integer correctOptionIndex;
    private List<TestOptionRequestDto> options;
}
