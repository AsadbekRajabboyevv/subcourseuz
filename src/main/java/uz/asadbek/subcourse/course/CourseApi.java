package uz.asadbek.subcourse.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseRequestDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseUpdateRequestDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeRequestDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeUpdateRequestDto;
import uz.asadbek.subcourse.course.grade.dto.OneCourseGradeResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonInfoResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonRequestDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonUpdateRequestDto;

@RequestMapping("/v1/api/courses")
@Tag(name = "Kurslar API")
public interface CourseApi {

    /// Course grade
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

    /// Course
    @GetMapping
    @Operation(summary = "Kurslarni qaytarish")
    BaseResponseDto<Page<CourseResponseDto>> get(CourseFilter filter, Pageable pageable);

    @GetMapping("/me")
    @Operation(summary = "A'zo bo'lgan kurslarni qaytarish (Sotib olingan va kursga qo'shilgan)")
    BaseResponseDto<Page<CourseResponseDto>> getMe(CourseFilter filter, Pageable pageable);

    @GetMapping("/{id}")
    @Operation(summary = "ID bo'yicha kursni qaytarish")
    BaseResponseDto<CourseInfoResponseDto> get(@PathVariable Long id);

    @PostMapping
    @Operation(summary = "Kurs yaratish")
    BaseResponseDto<Long> create(
        @RequestPart(required = false) @Valid CourseRequestDto request,
        @RequestPart MultipartFile image
    );

    @GetMapping("/update/{id}")
    @Operation(summary = "Kursni o'zgartirish uchun ma'lumotlarni olish")
    BaseResponseDto<CourseUpdateRequestDto> update(@PathVariable Long id);

    @PutMapping("/{id}")
    @Operation(summary = "Kursni o'zgartirish")
    BaseResponseDto<Long> update(
        @PathVariable Long id,
        @RequestPart(required = false) MultipartFile image,
        @RequestPart(required = false) CourseUpdateRequestDto request
    );

    @DeleteMapping("/{id}")
    @Operation(summary = "Kursni o'chirish")
    BaseResponseDto<Long> delete(@PathVariable Long id);


    ///  Lessons
    @PostMapping("/lessons")
    @Operation(summary = "Kursga dars qo'shish")
    BaseResponseDto<Long> saveLesson(
        @RequestPart @Valid CourseLessonRequestDto request,
        @RequestPart(required = false) List<MultipartFile> files
    );

    @PutMapping("/lessons/{id}")
    @Operation(summary = "Kurs darsni o'zgartirish")
    BaseResponseDto<Long> updateLesson(
        @PathVariable Long id,
        @RequestPart(required = false) CourseLessonUpdateRequestDto request,
        @RequestPart(required = false) List<MultipartFile> files,
        @RequestPart(required = false) List<String> deleteFileUrls
    );

    @DeleteMapping("/lessons/{id}")
    @Operation(summary = "Kurs darsni o'chirish")
    BaseResponseDto<Long> deleteLesson(@PathVariable Long id);

    @GetMapping("/lessons/{id}")
    @Operation(summary = "Kurs darsni ID bo'yicha qaytarish")
    BaseResponseDto<CourseLessonInfoResponseDto> getLesson(@PathVariable Long id);

    @GetMapping("/lessons/course/{id}")
    @Operation(summary = "Kurs darslarini KURS ID bo'yicha qaytarish")
    BaseResponseDto<List<CourseLessonResponseDto>> getLessonsByCourseId(@PathVariable Long id);
}
