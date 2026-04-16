package uz.asadbek.subcourse.course.grade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import uz.asadbek.subcourse.util.dto.DescriptionDto;
import uz.asadbek.subcourse.util.dto.NameDto;

@Data
@AllArgsConstructor
public class OneCourseGradeResponseDto {
    private Long id;
    private NameDto name;
    private DescriptionDto description;
}
