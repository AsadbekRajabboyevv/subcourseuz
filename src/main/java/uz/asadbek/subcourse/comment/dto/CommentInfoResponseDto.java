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

    public CommentInfoResponseDto(
        Long id,
        String text,
        String createdByName,
        Long createdBy,
        LocalDateTime createdAt
    ) {
        this.setCreatedAt(createdAt);
        this.setCreatedBy(createdBy);
        this.id = id;
        this.text = text;
        this.createdByName = createdByName;
    }
}
