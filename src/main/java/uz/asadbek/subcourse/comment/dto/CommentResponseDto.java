package uz.asadbek.subcourse.comment.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String text;
    private String createdByName;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Integer rating;
    private Long courseId;
    private Long lessonId;
    private Long testId;
}
