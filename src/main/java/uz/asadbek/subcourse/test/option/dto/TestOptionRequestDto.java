package uz.asadbek.subcourse.test.option.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TestOptionRequestDto {
    @NotNull
    private String text;
}
