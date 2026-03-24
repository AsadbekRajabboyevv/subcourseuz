package uz.asadbek.subcourse.course.grade;

import java.util.List;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeRequestDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeUpdateRequestDto;

public interface CourseGradeService {

    List<CourseGradeResponseDto> get();
    CourseGradeResponseDto create(CourseGradeRequestDto request);
    CourseGradeResponseDto update(Long id, CourseGradeUpdateRequestDto request);
    void delete(Long id);
}
