package uz.asadbek.subcourse.comment.filter;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentFilter {
    private Long id;
    private Long testId;
    private Long courseId;
    private Long lessonId;
    private Long createdBy;
    private Long updatedBy;
    private String createdByName;
    private String updatedByName;
    private String text;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
}
