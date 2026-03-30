package uz.asadbek.subcourse.test.session.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestSessionQuestionResponseDto {
    private Long questionId;
    private Integer orderIndex;

    private String text;
    private String imageUrl;
    private List<TestSessionOptionDto> options;
    private Long selectedOptionId;
    private Boolean answered;
}
