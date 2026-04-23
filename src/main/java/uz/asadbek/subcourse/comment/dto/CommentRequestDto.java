package uz.asadbek.subcourse.comment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequestDto {

    @NotBlank
    private String text;

    private Long courseId;
    private Long lessonId;
    private Long testId;

    @Min(1) @Max(5)
    private Integer rating;
}
