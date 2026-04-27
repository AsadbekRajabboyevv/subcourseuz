package uz.asadbek.subcourse.course;

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
import uz.asadbek.subcourse.course.filter.CourseFilter;
import uz.asadbek.subcourse.course.lesson.CourseLessonRepository;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;
import uz.asadbek.subcourse.course.usercourse.UserCourseEntity;
import uz.asadbek.subcourse.exception.BadRequestException;
import uz.asadbek.subcourse.exception.NotFoundException;
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
                throw ExceptionUtil.build(BadRequestException.class, "error.course.not_allowed");
            }
        }
        return repository.get(pageable, filter, LangUtils.currentLang(), null);
    }

    @Override
    public Page<CourseResponseDto> getMe(Pageable pageable, CourseFilter filter) {
        return repository.get(pageable, filter, LangUtils.currentLang(), JwtUtil.getCurrentUserId());
    }

    @Override
    public CourseInfoResponseDto getInfo(Long id) {
        var dto = repository.getCourseBasicInfo(id, LangUtils.currentLang())
            .orElseThrow(() -> ExceptionUtil.build(NotFoundException.class, "error.not_found.course", id));
        Boolean isPublished = true;
        if (JwtUtil.isAdmin()){
            isPublished = null;
        }
        var lessons = courseLessonRepository.findAllByCourseId(id, isPublished);
        dto.setLessons(lessons);
        dto.setLessonsCount((long) lessons.size());

        dto.setStudentsCount(userCourseRepository.countByIdReferenceId(id));

        if (JwtUtil.isAuthenticated()) {
            var currentUserId = JwtUtil.getCurrentUserId();
            var exists = userCourseRepository.existsByIdUserIdAndIdReferenceId(currentUserId, id);
            dto.setPurchased(exists);
        }

        return dto;
    }

    @Override
    public CourseResponseDto get(Long id) {
        return repository.get(id);
    }

    @Override
    public Boolean enroll(Long courseId) {
        var userId = JwtUtil.getCurrentUserId();
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
            var url = fileStorageService.upload(image, FileUploadOptions.COURSE_IMAGE).getUrl();
            entity.setImagePath(url);
        }

        return repository.save(entity).getId();
    }

    @Override
    public CourseUpdateRequestDto getUpdateData(Long id) {
        return repository.getUpdateData(id).orElseThrow(()-> ExceptionUtil.build(NotFoundException.class, "error.not_found.course", id));
    }

    @Override
    @Transactional
    public Long update(Long id, CourseUpdateRequestDto request, MultipartFile image) {
        var entity = findById(id);
        mapper.update(entity, request);
        if (image != null && !image.isEmpty()) {
            var url = fileStorageService.upload(image, FileUploadOptions.COURSE_IMAGE)
                .getUrl();
            if (entity.getImagePath() != null) {
                fileStorageService.softDelete(entity.getImagePath());
            }
            entity.setImagePath(url);
        }
        return repository.save(entity).getId();
    }

    @Override
    @Transactional
    public Long delete(Long id) {
        repository.delete(findById(id));
        return id;
    }

    private CourseEntity findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> ExceptionUtil.build(NotFoundException.class, "error.not_found.course", id));
    }

}
