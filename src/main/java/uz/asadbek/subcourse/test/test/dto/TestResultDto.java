package uz.asadbek.subcourse.test.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TestResultDto {

    private Double score;
    private Integer correctAnswers;
    private Integer totalQuestions;
}
