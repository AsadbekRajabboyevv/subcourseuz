package uz.asadbek.subcourse.test.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import uz.asadbek.subcourse.test.question.dto.TestQuestionResponseDto;

@Getter
@Setter
public class TestResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String lang;
    private Integer duration;
    private Boolean isPublished;
    private Long scienceId;
    private Long courseId;
    private Long lessonId;
    private Long gradeId;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TestQuestionResponseDto> questions;

    public TestResponseDto(
        Long id,
        String name,
        String description,
        Long price,
        String lang,
        Integer duration,
        Boolean isPublished,
        Long scienceId,
        Long courseId,
        Long lessonId,
        Long gradeId,
        String imagePath,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.lang = lang;
        this.duration = duration;
        this.isPublished = isPublished;
        this.scienceId = scienceId;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.gradeId = gradeId;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
