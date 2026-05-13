package uz.asadbek.subcourse.ai.dto;

import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GeminiTestGenerateResponseDto {
    private List<GeminiTestGenerateQuestionResponseDto> questions;
}
