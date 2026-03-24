package uz.asadbek.subcourse.publicui.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import uz.asadbek.subcourse.comment.dto.CommentResponseDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;

@Data
@AllArgsConstructor
public class HomePageResponseDto {

    private StatsDto stats;
    private List<CourseGradeResponseDto> courseGrades;
    private List<CourseResponseDto> topCourses;
    private List<CommentResponseDto> topComments;
}
