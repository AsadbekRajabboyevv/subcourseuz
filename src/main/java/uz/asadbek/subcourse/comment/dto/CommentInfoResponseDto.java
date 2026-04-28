package uz.asadbek.subcourse.comment.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import uz.asadbek.base.dto.BaseAuditResponseDto;

@Getter
@Setter
public class CommentInfoResponseDto extends BaseAuditResponseDto {

    private Long id;
    private String text;
    private String createdByName;
    private Integer rating;
    private Long courseId;
    private Long lessonId;
    private Long testId;

    public CommentInfoResponseDto(
        Long id, String text, String createdByName,
        Long createdBy, LocalDateTime createdAt,
        Integer rating, Long courseId, Long lessonId, Long testId
    ) {
        this.setCreatedBy(createdBy);
        this.setCreatedAt(createdAt);
        this.id = id;
        this.text = text;
        this.createdByName = createdByName;
        this.rating = rating;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.testId = testId;
    }
}
