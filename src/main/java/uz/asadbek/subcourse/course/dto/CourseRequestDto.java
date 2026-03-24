package uz.asadbek.subcourse.course.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uz.asadbek.subcourse.util.dto.DescriptionDto;
import uz.asadbek.subcourse.util.dto.NameDto;

@Data
public class CourseRequestDto {

    private NameDto name;

    private DescriptionDto description;

    @NotNull
    private Integer duration;

    @NotNull
    private String durationType;

    @NotNull
    private Long scienceId;

    @NotNull
    private Long gradeId;

    private Long price;

}
