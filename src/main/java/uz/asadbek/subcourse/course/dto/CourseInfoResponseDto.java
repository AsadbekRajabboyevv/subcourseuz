package uz.asadbek.subcourse.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseInfoResponseDto {
    private Long id;
    private String name;
    private String description;
    private String gradeName;
    private String scienceName;
    private Integer duration;
    private DurationType durationType;
    private Long lessonsCount;
    private Long studentsCount;
    private String ownerName;
    private Long price;
    private String imagePath;
    private String lang;
}
