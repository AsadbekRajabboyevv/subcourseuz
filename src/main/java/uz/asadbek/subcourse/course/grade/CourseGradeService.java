package uz.asadbek.subcourse.course.grade;

import java.util.List;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeRequestDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeUpdateRequestDto;
import uz.asadbek.subcourse.course.grade.dto.OneCourseGradeResponseDto;

public interface CourseGradeService {

    List<CourseGradeResponseDto> get();
    OneCourseGradeResponseDto get(Long id);
    CourseGradeResponseDto create(CourseGradeRequestDto request);
    CourseGradeResponseDto update(Long id, CourseGradeUpdateRequestDto request);
    void delete(Long id);
}
