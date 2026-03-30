package uz.asadbek.subcourse.test.option.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TestOptionUpdateDto {

    private Long id;

    private String text;

    private MultipartFile image;
}
