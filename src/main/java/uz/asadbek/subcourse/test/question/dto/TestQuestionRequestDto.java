package uz.asadbek.subcourse.test.question.dto;

import java.util.List;
import lombok.Data;
import uz.asadbek.subcourse.test.option.dto.TestOptionRequestDto;

@Data
public class TestQuestionRequestDto {
    private String text;
    private List<TestOptionRequestDto> options;
}
