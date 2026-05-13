package uz.asadbek.subcourse.comment;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.comment.dto.CommentInfoResponseDto;
import uz.asadbek.subcourse.comment.dto.CommentRequestDto;
import uz.asadbek.subcourse.comment.dto.CommentResponseDto;
import uz.asadbek.subcourse.comment.filter.CommentFilter;
import uz.asadbek.subcourse.exception.BadRequestException;
import uz.asadbek.subcourse.exception.ForbiddenException;
import uz.asadbek.subcourse.exception.NotFoundException;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final CommentMapper mapper;

    @Override
    public Page<CommentResponseDto> get(CommentFilter filter, Pageable pageable) {
        return repository.get(filter, pageable);
    }

    @Override
    public CommentInfoResponseDto get(Long id) {
        var comment = repository.get(id);
        if (comment == null) {
            throw ExceptionUtil.build(NotFoundException.class, "error.not_found.comment", id);
        }
        return comment;
    }

    @Override
    @Transactional
    public Long create(CommentRequestDto dto) {
        var currentUserId = JwtUtil.getCurrentUserId();

        if (dto.getCourseSlug() != null
            && repository.existsByCreatedByAndCourseSlug(currentUserId, dto.getCourseSlug())) {
            throw ExceptionUtil.build(BadRequestException.class, "error.comment.already_exists_for_course");
        }
        if (dto.getLessonId() != null
            && repository.existsByCreatedByAndLessonId(currentUserId, dto.getLessonId())) {
            throw ExceptionUtil.build(BadRequestException.class, "error.comment.already_exists_for_lesson");
        }
        if (dto.getTestId() != null
            && repository.existsByCreatedByAndTestId(currentUserId, dto.getTestId())) {
            throw ExceptionUtil.build(BadRequestException.class, "error.comment.already_exists_for_test");
        }

        var entity = mapper.toEntity(dto);
        var saved = repository.save(entity);
        log.info("Comment created: id={}, userId={}", saved.getId(), currentUserId);
        return saved.getId();
    }

    @Override
    @Transactional
    public Long update(Long id, CommentRequestDto dto) {
        var currentUserId = JwtUtil.getCurrentUserId();
        var entity = repository.findById(id)
            .orElseThrow(() -> ExceptionUtil.build(NotFoundException.class, "error.not_found.comment", id));

        // Faqat o'z izohini o'zgartira oladi (yoki admin)
        if (!entity.getCreatedBy().equals(currentUserId) && !JwtUtil.isAdmin()) {
            throw ExceptionUtil.build(ForbiddenException.class, "error.forbidden.comment_update");
        }

        mapper.update(entity, dto);
        repository.save(entity);
        log.info("Comment updated: id={}, userId={}", id, currentUserId);
        return id;
    }

    @Override
    @Transactional
    public Long delete(Long id) {
        var currentUserId = JwtUtil.getCurrentUserId();
        var entity = repository.findById(id)
            .orElseThrow(() -> ExceptionUtil.build(NotFoundException.class, "error.not_found.comment", id));

        // Faqat o'z izohini yoki admin o'chira oladi
        if (!entity.getCreatedBy().equals(currentUserId) && !JwtUtil.isAdmin()) {
            throw ExceptionUtil.build(ForbiddenException.class, "error.forbidden.comment_delete");
        }

        repository.delete(entity);
        log.info("Comment deleted: id={}, userId={}", id, currentUserId);
        return id;
    }

    @Override
    public Double getAvgRating(String courseSlug, Long lessonId, Long testId) {
        Double avg = repository.avgRating(courseSlug, lessonId, testId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    @Override
    public List<CommentResponseDto> getTop() {
        return repository.getTop();
    }
}
