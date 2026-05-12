package uz.asadbek.subcourse.ai.dto;

import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GeminiTestGenerateQuestionResponseDto {

    private String text;
    private Integer correctOptionIndex;
    private List<GeminiTestGenerationOptionResponseDto> options;
}
