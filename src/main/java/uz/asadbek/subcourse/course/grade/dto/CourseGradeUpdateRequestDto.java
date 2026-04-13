package uz.asadbek.subcourse.course.grade.dto;

import lombok.Data;
import uz.asadbek.subcourse.util.dto.DescriptionDto;
import uz.asadbek.subcourse.util.dto.NameDto;

@Data
public class CourseGradeUpdateRequestDto {
    private NameDtoOptional name;
    private DescriptionDto description;
}
