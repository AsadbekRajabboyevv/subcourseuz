package uz.asadbek.subcourse.course.dto;

import lombok.Data;

@Data
public class CourseUpdateRequestDto {
    private String name;
    private String description;
    private Integer duration;
    private String durationType;
    private Long scienceId;
    private Long gradeId;
    private Long price;
    private String lang;
    private Boolean isPublished;
}
