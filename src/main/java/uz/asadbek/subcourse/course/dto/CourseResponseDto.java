package uz.asadbek.subcourse.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseResponseDto {
    private Long id;
    private String name;
    private Long lessonsCount;
    private Long studentsCount;
    private String ownerName;
    private Long price;
    private String imagePath;
    private String lang;
    private Boolean isPublished;
}
