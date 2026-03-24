package uz.asadbek.subcourse.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestReviewDto {

    private Long questionId;
    private String questionText;
    private Long selectedOptionId;
    private String selectedOptionText;
    private boolean wrong;
}
