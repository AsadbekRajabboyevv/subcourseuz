package uz.asadbek.subcourse.course.lesson;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonInfoResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonRequestDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonUpdateRequestDto;
import uz.asadbek.subcourse.filestorage.FileStorageService;
import uz.asadbek.subcourse.filestorage.dto.FileUploadOptions;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseLessonServiceImpl implements CourseLessonService {

    private final CourseLessonRepository repository;
    private final CourseLessonMapper mapper;
    private final FileStorageService fileStorageService;

    @Override
    public List<CourseLessonResponseDto> getByCourseId(Long courseId) {
        return repository.getByCourseId(courseId);
    }

    @Override
    public CourseLessonInfoResponseDto get(Long id) {
        return repository.get(id);
    }

    @Override
    @Transactional
    public Long create(CourseLessonRequestDto request, List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (var file : files) {
                if (!file.isEmpty()) {
                    var upload = fileStorageService.upload(file,
                        FileUploadOptions.LESSON_FILE);
                    urls.add(upload.getUrl());
                }
            }
        }
        var entity = mapper.toEntity(request);
        entity.setFileUrls(urls);

        return repository.save(entity).getId();
    }
    @Override
    @Transactional
    public Long update(Long id, CourseLessonUpdateRequestDto dto, List<MultipartFile> files, List<String> deletedFileUrls) {
        var entity = findById(id);
        mapper.update(entity, dto);

        if (deletedFileUrls != null && !deletedFileUrls.isEmpty()) {
            entity.getFileUrls().removeAll(deletedFileUrls);
            for (var url : deletedFileUrls) {
                fileStorageService.softDelete(url);
            }
        }

        if (files != null && !files.isEmpty()) {
            for (var file : files) {
                if (!file.isEmpty()) {
                    var upload = fileStorageService.upload(file,
                        FileUploadOptions.LESSON_FILE);
                    entity.getFileUrls().add(upload.getUrl());
                }
            }
        }

        return repository.save(entity).getId();
    }

    private CourseLessonEntity findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> ExceptionUtil.notFoundException("course_lesson_not_found"));
    }

    @Override
    public Long delete(Long id) {
        var entity = findById(id);
        repository.delete(entity);
        return id;
    }

    @Override
    public Long videoCoursesCount() {
        return repository.countCourseLessonEntitiesByDeletedAtIsNullAndVideoUrlIsNotNullAndIsPublishedIsTrue();
    }

}
