package uz.asadbek.subcourse.course.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private boolean purchased;
    private List<CourseLessonResponseDto> lessons;
    private Long scienceId;
    private Long gradeId;

    public CourseInfoResponseDto(Long id, String name, String description, String gradeName,
        String scienceName, Integer duration, DurationType durationType,
        String ownerName, Long price, String imagePath, String lang,
        Long scienceId, Long gradeId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gradeName = gradeName;
        this.scienceName = scienceName;
        this.duration = duration;
        this.durationType = durationType;
        this.ownerName = ownerName;
        this.price = price;
        this.imagePath = imagePath;
        this.lang = lang;
        this.scienceId = scienceId;
        this.gradeId = gradeId;
    }
}
