package uz.asadbek.subcourse.test.test.dto;

import lombok.Data;

@Data
public class TestReviewQuestionDto {
    private String questionText;
    private String imagePath;
    private Integer selectedOptionOrderNumber;
    private Boolean isCorrect;
    private TestReviewOptionDto options;
}
