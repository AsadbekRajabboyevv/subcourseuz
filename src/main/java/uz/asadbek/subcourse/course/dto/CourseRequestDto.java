package uz.asadbek.subcourse.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uz.asadbek.subcourse.course.grade.CourseGradeEntity;
import uz.asadbek.subcourse.science.ScienceEntity;
import uz.asadbek.subcourse.util.annotation.existindb.ExistsInDb;

@Data
public class CourseRequestDto {

    @NotNull
    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Min(value = 1)
    private Integer duration;

    @NotNull
    private String durationType;

    @NotNull
    @ExistsInDb(entity = ScienceEntity.class)
    private Long scienceId;

    @NotNull
    @ExistsInDb(entity = CourseGradeEntity.class)
    private Long gradeId;

    private Long price;

    private String lang;

    private Boolean isVideoCourse;

    private Boolean isPublished;

}
