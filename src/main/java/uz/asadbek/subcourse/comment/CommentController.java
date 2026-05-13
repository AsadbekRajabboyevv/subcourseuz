package uz.asadbek.subcourse.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.comment.dto.CommentInfoResponseDto;
import uz.asadbek.subcourse.comment.dto.CommentRequestDto;
import uz.asadbek.subcourse.comment.dto.CommentResponseDto;
import uz.asadbek.subcourse.comment.filter.CommentFilter;

@RestController
@RequiredArgsConstructor
public class CommentController implements CommentApi {

    private final CommentService service;

    @Override
    public BaseResponseDto<Page<CommentResponseDto>> get(CommentFilter filter, Pageable pageable) {
        return BaseResponseDto.ok(service.get(filter, pageable));
    }

    @Override
    public BaseResponseDto<CommentInfoResponseDto> get(Long id) {
        return BaseResponseDto.ok(service.get(id));
    }

    @Override
    public BaseResponseDto<Double> getAvgRating(String courseSlug, Long lessonId, Long testId) {
        return BaseResponseDto.ok(service.getAvgRating(courseSlug, lessonId, testId));
    }

    @Override
    public BaseResponseDto<Long> create(CommentRequestDto dto) {
        return BaseResponseDto.ok(service.create(dto));
    }

    @Override
    public BaseResponseDto<Long> update(Long id, CommentRequestDto dto) {
        return BaseResponseDto.ok(service.update(id, dto));
    }

    @Override
    public BaseResponseDto<Long> delete(Long id) {
        return BaseResponseDto.ok(service.delete(id));
    }
}
