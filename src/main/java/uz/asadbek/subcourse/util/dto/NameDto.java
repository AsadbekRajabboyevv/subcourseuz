package uz.asadbek.subcourse.util.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NameDto {

    @NotNull
    @NotEmpty
    private String nameUz;

    @NotNull
    @NotEmpty
    private String nameEn;

    @NotNull
    @NotEmpty
    private String nameRu;

    @NotNull
    @NotEmpty
    private String nameCrl;
}
