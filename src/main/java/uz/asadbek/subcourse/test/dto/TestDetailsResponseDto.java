package uz.asadbek.subcourse.test.dto;

import java.time.LocalDateTime;
import java.util.List;
import uz.asadbek.subcourse.test.question.dto.TestQuestionResponseDto;

public class TestDetailsResponseDto {
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

    private String imageUrl;

    private List<TestQuestionResponseDto> questions;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
