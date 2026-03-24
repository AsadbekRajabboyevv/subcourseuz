package uz.asadbek.subcourse.course.lesson.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseLessonInfoResponseDto {

    private Long id;
    private String name;
    private Integer lessonNumber;
    private String videoUrl;
    private String courseName;
    private String courseImagePath;
    private String textContent;
}
