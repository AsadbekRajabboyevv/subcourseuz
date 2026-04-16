package uz.asadbek.subcourse.test.dto;

import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.test.question.dto.TestQuestionUpdateRequestDto;

@Data
public class TestUpdateRequestDto {
    private String name;
    private String description;
    private Long price;
    private String lang;
    private Long lessonId;
    private Long courseId;
    private Long scienceId;
    private Long gradeId;
    private Integer duration;
    private Boolean isPublished;

    private MultipartFile image;

    private List<TestQuestionUpdateRequestDto> questions;
}
