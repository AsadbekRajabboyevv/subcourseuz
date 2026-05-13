package uz.asadbek.subcourse.comment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uz.asadbek.subcourse.comment.validation.HasTarget;

@Data
@HasTarget
public class CommentRequestDto {

    @NotBlank(message = "Izoh matni bo'sh bo'lmasligi kerak")
    private String text;

    @NotNull(message = "Reyting majburiy")
    @Min(value = 1, message = "Reyting 1 dan kam bo'lmasligi kerak")
    @Max(value = 5, message = "Reyting 5 dan oshmasligi kerak")
    private Integer rating;

    private String courseSlug;
    private Long lessonId;
    private Long testId;
}
