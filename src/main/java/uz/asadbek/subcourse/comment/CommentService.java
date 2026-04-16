package uz.asadbek.subcourse.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.asadbek.subcourse.comment.dto.CommentInfoResponseDto;
import uz.asadbek.subcourse.comment.dto.CommentRequestDto;
import uz.asadbek.subcourse.comment.dto.CommentResponseDto;
import uz.asadbek.subcourse.comment.filter.CommentFilter;

public interface CommentService {

    Page<CommentResponseDto> get(CommentFilter filter, Pageable pageable);
    CommentInfoResponseDto get(Long id);
    Long create(CommentRequestDto dto);
    Long update(Long id, CommentRequestDto dto);
    Long delete(Long id);
}
