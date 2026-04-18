package uz.asadbek.subcourse.course.lesson.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import uz.asadbek.subcourse.course.lesson.CourseLessonEntity;
import uz.asadbek.subcourse.util.annotation.existindb.ExistsInDb;

@Getter
public class CourseLessonRequestDto {

    @NotNull
    @Max(value = 500)
    private String name;

    @NotNull
    @Max(value = 100)
    @Min(value = 1)
    private Integer lessonNumber;
    private String videoUrl;
    private String textContent;

    @NotNull
    @ExistsInDb(entity = CourseLessonEntity.class)
    private Long courseId;

    @NotNull
    private Boolean isPublished;
}
