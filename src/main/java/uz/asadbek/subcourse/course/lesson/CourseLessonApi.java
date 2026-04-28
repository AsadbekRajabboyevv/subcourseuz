package uz.asadbek.subcourse.course.lesson;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonInfoResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonRequestDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonUpdateRequestDto;

public interface CourseLessonApi {
    @PostMapping
    @Operation(summary = "Kursga dars qo'shish")
    BaseResponseDto<Long> saveLesson(
        @RequestPart @Valid CourseLessonRequestDto request,
        @RequestPart(required = false) List<MultipartFile> files
    );

    @PutMapping("/{id}")
    @Operation(summary = "Kurs darsni o'zgartirish")
    BaseResponseDto<Long> updateLesson(
        @PathVariable Long id,
        @RequestPart(required = false) CourseLessonUpdateRequestDto request,
        @RequestPart(required = false) List<MultipartFile> files,
        @RequestPart(required = false) List<String> deleteFileUrls
    );

    @DeleteMapping("/{id}")
    @Operation(summary = "Kurs darsni o'chirish")
    BaseResponseDto<Long> deleteLesson(@PathVariable Long id);

    @GetMapping("/{id}")
    @Operation(summary = "Kurs darsni ID bo'yicha qaytarish")
    BaseResponseDto<CourseLessonInfoResponseDto> getLesson(@PathVariable Long id);

    @GetMapping("/course/{id}")
    @Operation(summary = "Kurs darslarini KURS ID bo'yicha qaytarish")
    BaseResponseDto<List<CourseLessonResponseDto>> getLessonsByCourseId(@PathVariable Long id);}
