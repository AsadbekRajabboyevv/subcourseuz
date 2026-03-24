package uz.asadbek.subcourse.course;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeRequestDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeUpdateRequestDto;

@RequestMapping("/v1/api/courses")
public interface CourseApi {

    @GetMapping("/grades")
    BaseResponseDto<List<CourseGradeResponseDto>> getCourseGrades();

    @PostMapping("/grades")
    BaseResponseDto<CourseGradeResponseDto> saveCourseGrade(@RequestBody @Valid CourseGradeRequestDto request);

    @PutMapping("/grades")
    BaseResponseDto<CourseGradeResponseDto> updateCourseGrade(Long id, @RequestBody CourseGradeUpdateRequestDto request);

    @DeleteMapping("/grades/{id}")
    BaseResponseDto<?> deleteCourseGrade(@PathVariable Long id);

    @GetMapping
    BaseResponseDto<Page<CourseResponseDto>> get(CourseFilter filter, Pageable pageable);

    @GetMapping("/{id}")
    BaseResponseDto<CourseInfoResponseDto> get(@PathVariable Long id);

    @PostMapping("/enroll/{id}")
    BaseResponseDto<Boolean> enroll(@PathVariable Long id);
}
