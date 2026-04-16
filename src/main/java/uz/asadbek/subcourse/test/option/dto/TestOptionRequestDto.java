package uz.asadbek.subcourse.test.option.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TestOptionRequestDto {
    @NotNull
    private String text;
    private MultipartFile image;
}
