package uz.asadbek.subcourse.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequestDto {

    @NotNull
    @NotBlank
    @NotEmpty
    private String text;
}
