package uz.asadbek.subcourse.science.dto;

import lombok.Data;
import uz.asadbek.subcourse.course.grade.dto.NameDtoOptional;
import uz.asadbek.subcourse.util.dto.DescriptionDto;

@Data
public class ScienceUpdateRequestDto {
    private Long id;
    private NameDtoOptional name;
    private DescriptionDto description;
}
