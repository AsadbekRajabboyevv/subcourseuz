package uz.asadbek.subcourse.course.lesson.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import uz.asadbek.subcourse.course.CourseEntity;
import uz.asadbek.subcourse.course.lesson.CourseLessonEntity;
import uz.asadbek.subcourse.util.annotation.existindb.ExistsInDb;

@Getter
public class CourseLessonRequestDto {

    @NotNull
    private String name;

    @NotNull
    @Max(value = 100)
    @Min(value = 1)
    private Integer lessonNumber;
    private String videoUrl;
    private String textContent;

    @NotNull
    @ExistsInDb(
        entity = CourseEntity.class,
        field = "slug"
    )
    private String slug;

    @NotNull
    private Boolean isPublished;
}
