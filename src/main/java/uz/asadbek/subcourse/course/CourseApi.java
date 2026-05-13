package uz.asadbek.subcourse.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseRequestDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseUpdateRequestDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;

@RequestMapping("/v1/api/courses")
@Tag(name = "Kurslar", description = "Kurslar")
public interface CourseApi {

    @GetMapping
    @Operation(summary = "Kurslarni qaytarish")
    BaseResponseDto<Page<CourseResponseDto>> get(CourseFilter filter, Pageable pageable);

    @GetMapping("/me")
    @Operation(summary = "A'zo bo'lgan kurslarni qaytarish (Sotib olingan va kursga qo'shilgan)")
    BaseResponseDto<Page<CourseResponseDto>> getMe(CourseFilter filter, Pageable pageable);

    @GetMapping("/{slug}")
    @Operation(summary = "ID bo'yicha kursni qaytarish")
    BaseResponseDto<CourseInfoResponseDto> get(@PathVariable String slug);

    @PostMapping
    @Operation(summary = "Kurs yaratish")
    BaseResponseDto<String> create(
        @RequestPart(required = false) @Valid CourseRequestDto request,
        @RequestPart MultipartFile image
    );

    @GetMapping("/update/{slug}")
    @Operation(summary = "Kursni o'zgartirish uchun ma'lumotlarni olish")
    BaseResponseDto<CourseUpdateRequestDto> update(@PathVariable String slug);

    @PutMapping("/{slug}")
    @Operation(summary = "Kursni o'zgartirish")
    BaseResponseDto<String> update(
        @PathVariable String slug,
        @RequestPart(required = false) MultipartFile image,
        @RequestPart(required = false) CourseUpdateRequestDto request
    );

    @DeleteMapping("/{slug}")
    @Operation(summary = "Kursni o'chirish")
    BaseResponseDto<String> delete(@PathVariable String slug);

}
