package uz.asadbek.subcourse.comment;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.comment.dto.CommentInfoResponseDto;
import uz.asadbek.subcourse.comment.dto.CommentRequestDto;
import uz.asadbek.subcourse.comment.dto.CommentResponseDto;
import uz.asadbek.subcourse.comment.filter.CommentFilter;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final CommentMapper mapper;
    private static final Integer LIMIT_DAYS = 1;

    @Override
    public Page<CommentResponseDto> get(CommentFilter filter, Pageable pageable) {
        return null;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CommentInfoResponseDto get(Long id) {
        return repository.get(id);
    }

    @Override
    @Transactional
    public Long create(CommentRequestDto dto) {
        var currentUserId = JwtUtil.getCurrentUser().getId();
        var latestComment = repository.findFirstByCreatedByOrderByCreatedAtDesc(currentUserId);
        if (latestComment.isPresent()) {
            var lastCreatedAt = latestComment.get().getCreatedAt();
            var limitTime = LocalDateTime.now().minusDays(LIMIT_DAYS);

            if (lastCreatedAt.isAfter(limitTime)) {
                log.info("User {} exceeded comment limit", currentUserId);
                throw ExceptionUtil.badRequestException("comment_limit_exceeded");
            }
        }
        var entity = mapper.toEntity(dto);
        return repository.save(entity).getId();
    }

    @Override
    @Transactional
    public Long update(Long id, CommentRequestDto dto) {
        var entity = repository.findById(id).orElseThrow(()-> ExceptionUtil.notFoundException("comment_not_found"));
        mapper.update(entity, dto);
        return repository.save(entity).getId();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long delete(Long id) {
        var entity = repository.findById(id).orElseThrow(()-> ExceptionUtil.notFoundException("comment_not_found"));
        repository.delete(entity);
        return id;
    }
}
