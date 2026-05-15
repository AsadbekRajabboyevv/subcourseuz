package uz.asadbek.subcourse.test.test.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestReviewQuestionDto {
    private String questionText;
    private String imagePath;
    private Integer selectedOptionOrderNumber;
    private Boolean isCorrect;
    private Long correctOptionId;
    private List<TestReviewOptionDto> options;
}
