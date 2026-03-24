package uz.asadbek.subcourse.course.lesson.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CourseLessonRequestDto {

    @NotNull
    private String name;
    @NotNull
    @Min(value = 1)
    private Integer lessonNumber;
    private String videoUrl;
    private String textContent;
    @NotNull
    private Long courseId;
}
