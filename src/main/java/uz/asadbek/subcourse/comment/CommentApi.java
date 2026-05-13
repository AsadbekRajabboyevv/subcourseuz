package uz.asadbek.subcourse.comment;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.comment.dto.CommentInfoResponseDto;
import uz.asadbek.subcourse.comment.dto.CommentRequestDto;
import uz.asadbek.subcourse.comment.dto.CommentResponseDto;
import uz.asadbek.subcourse.comment.filter.CommentFilter;

@RequestMapping("/v1/api/comments")
@Tag(name = "Izohlar API")
public interface CommentApi {

    @GetMapping
    @Operation(summary = "Izohlarni sahifalab olish (filter bilan)")
    BaseResponseDto<Page<CommentResponseDto>> get(CommentFilter filter, Pageable pageable);

    @GetMapping("/{id}")
    @Operation(summary = "ID bo'yicha izohni olish")
    BaseResponseDto<CommentInfoResponseDto> get(@PathVariable Long id);

    @GetMapping("/avg-rating")
    @Operation(summary = "O'rtacha reytingni olish")
    BaseResponseDto<Double> getAvgRating(
        @RequestParam(required = false) String courseSlug,
        @RequestParam(required = false) Long lessonId,
        @RequestParam(required = false) Long testId
    );

    @PostMapping
    @Operation(summary = "Izoh qo'shish (courseSlug, lessonId yoki testId majburiy)")
    BaseResponseDto<Long> create(@RequestBody @Valid CommentRequestDto dto);

    @PutMapping("/{id}")
    @Operation(summary = "Izohni tahrirlash (faqat o'z izohi)")
    BaseResponseDto<Long> update(@PathVariable Long id, @RequestBody @Valid CommentRequestDto dto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Izohni o'chirish (o'z izohi yoki admin)")
    BaseResponseDto<Long> delete(@PathVariable Long id);
}
