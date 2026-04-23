package uz.asadbek.subcourse.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseUpdateRequestDto {
    private String name;
    private String description;
    private Integer duration;
    private DurationType durationType;
    private Long scienceId;
    private Long gradeId;
    private Long price;
    private String lang;
    private Boolean isPublished;
    private Boolean isVideoCourse;
    private String imagePath;
}
