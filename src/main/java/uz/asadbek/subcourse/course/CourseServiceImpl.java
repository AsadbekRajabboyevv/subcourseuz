package uz.asadbek.subcourse.course;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseRequestDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseUpdateRequestDto;
import uz.asadbek.subcourse.course.dto.DurationType;
import uz.asadbek.subcourse.course.filter.CourseFilter;
import uz.asadbek.subcourse.course.lesson.CourseLessonRepository;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;
import uz.asadbek.subcourse.course.usercourse.UserCourseEntity;
import uz.asadbek.subcourse.filestorage.FileStorageService;
import uz.asadbek.subcourse.filestorage.dto.FileUploadOptions;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;
import uz.asadbek.subcourse.util.Validator;
import uz.asadbek.subcourse.util.embedded.UserPurchaseId;
import uz.asadbek.subcourse.course.usercourse.UserCourseRepository;
import uz.asadbek.subcourse.util.LangUtils;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;
    private final UserCourseRepository userCourseRepository;
    private final Validator validator;
    private final CourseMapper mapper;
    private final FileStorageService fileStorageService;
    private final CourseLessonRepository courseLessonRepository;

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public Page<CourseResponseDto> getInfo(Pageable pageable, CourseFilter filter) {
        if (filter.getIsPublished()) {
            if (JwtUtil.isAuthenticated() && JwtUtil.isStudent()) {
                throw ExceptionUtil.forbiddenException("not_allowed");
            }
        }
        return repository.get(pageable, filter, LangUtils.currentLang(), null);
    }

    @Override
    public Page<CourseResponseDto> getMe(Pageable pageable, CourseFilter filter) {
        return repository.get(pageable, filter, LangUtils.currentLang(), JwtUtil.getCurrentUser().getId());
    }

    @Override
    public CourseInfoResponseDto getInfo(Long id) {
        CourseInfoResponseDto dto = repository.getCourseBasicInfo(id, LangUtils.currentLang())
            .orElseThrow(() -> ExceptionUtil.notFoundException("course_not_found"));

        List<CourseLessonResponseDto> lessons = courseLessonRepository.findAllByCourseId(id);
        dto.setLessons(lessons);
        dto.setLessonsCount((long) lessons.size());

        dto.setStudentsCount(userCourseRepository.countByIdReferenceId(id));

        if (JwtUtil.isAuthenticated()) {
            Long currentUserId = JwtUtil.getCurrentUser().getId();
            boolean exists = userCourseRepository.existsByIdUserIdAndIdReferenceId(currentUserId, id);
            dto.setPurchased(exists);
        }

        return dto;
    }

    private Long convertToLong(Object obj) {
        if (obj == null) return 0L;
        if (obj instanceof Number) return ((Number) obj).longValue();
        return 0L;
    }

    @Override
    public CourseResponseDto get(Long id) {
        return repository.get(id);
    }

    @Override
    public Boolean enroll(Long courseId) {
        var userId = JwtUtil.getCurrentUser().getId();
        validator.validateEnroll(userId, courseId, repository::existsById, "course_not_found");
        var uc = new UserCourseEntity();
        uc.setId(new UserPurchaseId(userId, courseId, LocalDateTime.now()));

        userCourseRepository.save(uc);
        return true;
    }

    @Override
    @Transactional
    public Long create(MultipartFile image, CourseRequestDto request) {
        var entity = mapper.toEntity(request);

        if (image != null && !image.isEmpty()) {
            var url = fileStorageService.upload(image, new FileUploadOptions().setCourseImages())
                .getUrl();
            entity.setImagePath(url);
        }

        return repository.save(entity).getId();
    }

    @Override
    public CourseUpdateRequestDto getUpdateData(Long id) {
        return repository.getUpdateData(id).orElseThrow(()-> ExceptionUtil.notFoundException("course_not_found"));
    }

    @Override
    @Transactional
    public Long update(Long id, CourseUpdateRequestDto request, MultipartFile image) {
        var entity = repository.findById(id).orElseThrow(()-> ExceptionUtil.notFoundException("course_not_found"));
        mapper.update(entity, request);
        if (image != null && !image.isEmpty()) {
            var url = fileStorageService.upload(image, new FileUploadOptions().setCourseImages())
                .getUrl();
            fileStorageService.delete(entity.getImagePath());
            entity.setImagePath(url);
        }
        return repository.save(entity).getId();
    }

    @Override
    @Transactional
    public Long delete(Long id) {
        CourseEntity ent = repository.findById(id)
            .orElseThrow(() -> ExceptionUtil.notFoundException("course_not_found"));
        repository.delete(ent);
        return id;
    }

}
