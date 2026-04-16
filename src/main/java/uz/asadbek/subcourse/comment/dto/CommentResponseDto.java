package uz.asadbek.subcourse.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDto {

    private String text;
    private String createdByName;
    private String createdAt;
}
