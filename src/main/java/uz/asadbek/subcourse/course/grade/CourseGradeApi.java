package uz.asadbek.subcourse.course.grade;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeRequestDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeUpdateRequestDto;
import uz.asadbek.subcourse.course.grade.dto.OneCourseGradeResponseDto;

public interface CourseGradeApi {
    @GetMapping("/grades")
    @Operation(summary = "Kurs darajalarini qaytarish")
    BaseResponseDto<List<CourseGradeResponseDto>> getCourseGrades();

    @PostMapping("/grades")
    @Operation(summary = "Kurs darajasini yaratish")
    BaseResponseDto<CourseGradeResponseDto> saveCourseGrade(
        @RequestBody @Valid CourseGradeRequestDto request);

    @PutMapping("/grades/{id}")
    @Operation(summary = "Kurs darajasini o'zgartirish")
    BaseResponseDto<CourseGradeResponseDto> updateCourseGrade(@PathVariable Long id,
        @RequestBody CourseGradeUpdateRequestDto request);

    @DeleteMapping("/grades/{id}")
    @Operation(summary = "ID bo'yicha kurs darajasini o'chirish")
    BaseResponseDto<?> deleteCourseGrade(@PathVariable Long id);

    @GetMapping("/grades/{id}")
    @Operation(summary = "ID bo'yicha kurs darajasini qaytarish")
    BaseResponseDto<OneCourseGradeResponseDto> getCourseGrade(@PathVariable Long id);

}
