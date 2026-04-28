package uz.asadbek.subcourse.course.lesson.dto;

import lombok.Data;

@Data
public class CourseLessonUpdateRequestDto {
    private String name;
    private Integer lessonNumber;
    private String videoUrl;
    private String textContent;
    private Long courseId;
    private Boolean isPublished;
}
