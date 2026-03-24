package uz.asadbek.subcourse.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestResultDto {

    private Double score;
    private Integer correctAnswers;
    private Integer totalQuestions;
}
