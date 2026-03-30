package uz.asadbek.subcourse.test.question.dto;

import java.util.List;
import lombok.Data;
import uz.asadbek.subcourse.test.option.dto.TestOptionResponseDto;

@Data
public class TestQuestionResponseDto {
    private Long id;
    private String text;
    private String imageUrl;
    private Long correctOptionId;
    private List<TestOptionResponseDto> options;
}
