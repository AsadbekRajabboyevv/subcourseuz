package uz.asadbek.subcourse.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
    private Long scienceId;

    @NotNull
    private Long gradeId;

    private Long price;

}
