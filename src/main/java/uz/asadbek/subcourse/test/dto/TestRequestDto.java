package uz.asadbek.subcourse.test.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.course.CourseEntity;
import uz.asadbek.subcourse.course.grade.CourseGradeEntity;
import uz.asadbek.subcourse.course.lesson.CourseLessonEntity;
import uz.asadbek.subcourse.science.ScienceEntity;
import uz.asadbek.subcourse.test.question.dto.TestQuestionRequestDto;
import uz.asadbek.subcourse.util.annotation.Severity;
import uz.asadbek.subcourse.util.annotation.existindb.ExistsInDb;

@Data
public class TestRequestDto {

    @NotNull
    private String name;

    private String description;

    private Long price;

    @NotNull
    private String lang;

    @ExistsInDb(
        entity = CourseLessonEntity.class,
        optional = true,
        setNull = true,
        payload = {
            Severity.WARNING.class
        }
    )
    private Long lessonId;

    @ExistsInDb(
        entity = CourseEntity.class,
        optional = true,
        setNull = true,
        payload = {
            Severity.WARNING.class
        }
    )
    private Long courseId;

    @NotNull
    @ExistsInDb(entity = ScienceEntity.class)
    private Long scienceId;

    @ExistsInDb(
        entity = CourseGradeEntity.class,
        optional = true,
        setNull = true,
        payload = {
            Severity.WARNING.class
        }
    )
    private Long gradeId;

    @NotNull
    private Integer duration;

    private Boolean isPublished;

    private List<TestQuestionRequestDto> questions;
}
