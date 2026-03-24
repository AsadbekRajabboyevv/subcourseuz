package uz.asadbek.subcourse.course.grade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseGradeResponseDto {

    private Long id;
    private String name;
    private String description;
}
