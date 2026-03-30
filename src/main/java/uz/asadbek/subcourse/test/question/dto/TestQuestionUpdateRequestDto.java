package uz.asadbek.subcourse.test.question.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.test.option.dto.TestOptionUpdateDto;
import java.util.List;

@Data
public class TestQuestionUpdateRequestDto {
    private Long id;

    private String text;

    private MultipartFile image;

    private Integer correctOptionIndex;

    private List<TestOptionUpdateDto> options;
}
