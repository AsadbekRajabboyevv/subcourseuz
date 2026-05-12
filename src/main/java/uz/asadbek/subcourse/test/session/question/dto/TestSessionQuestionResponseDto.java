package uz.asadbek.subcourse.test.session.question.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.asadbek.subcourse.test.session.option.dto.TestSessionOptionResponseDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestSessionQuestionResponseDto {
    private Long id;
    private Integer orderNumber;
    private String text;
    private String imagePath;
    private Long selectedOptionId;
    private Boolean answered;
    @JsonIgnore
    private Long correctOptionId;
    private List<TestSessionOptionResponseDto> options;

    public TestSessionQuestionResponseDto(Long id, Integer orderNumber, String text,
        String imagePath,
        Long selectedOptionId, Boolean answered, Long correctOptionId) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.text = text;
        this.imagePath = imagePath;
        this.selectedOptionId = selectedOptionId;
        this.answered = answered;
        this.correctOptionId = correctOptionId;
    }
}
