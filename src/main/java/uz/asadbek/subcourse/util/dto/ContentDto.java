package uz.asadbek.subcourse.util.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContentDto {
    @NotNull
    @NotEmpty
    private String contentUz;
    @NotNull
    @NotEmpty
    private String contentEn;
    @NotNull
    @NotEmpty
    private String contentRu;
    @NotNull
    @NotEmpty
    private String contentCrl;
}
