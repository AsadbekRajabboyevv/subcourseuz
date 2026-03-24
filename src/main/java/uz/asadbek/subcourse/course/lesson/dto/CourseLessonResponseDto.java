package uz.asadbek.subcourse.course.lesson.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseLessonResponseDto {
    private Long id;
    private String name;
    private Integer lessonNumber;
}
