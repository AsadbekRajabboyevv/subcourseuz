package uz.asadbek.subcourse.test.question.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import uz.asadbek.subcourse.test.option.dto.TestOptionResponseDto;

@Data
@AllArgsConstructor
public class TestQuestionResponseDto {

    private Long id;
    private String text;
    private String imagePath;
    private List<TestOptionResponseDto> options;

    public TestQuestionResponseDto(
        Long id,
        String text,
        String imagePath
    ) {
        this.id = id;
        this.text = text;
        this.imagePath = imagePath;
    }
}
