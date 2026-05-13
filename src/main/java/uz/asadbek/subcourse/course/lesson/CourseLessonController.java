package uz.asadbek.subcourse.course.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonInfoResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonRequestDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonUpdateRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseLessonController implements CourseLessonApi {

    private final CourseLessonService service;

    @Override
    public BaseResponseDto<Long> saveLesson(CourseLessonRequestDto request, List<MultipartFile> files) {
        return BaseResponseDto.ok(service.create(request, files));
    }

    @Override
    public BaseResponseDto<Long> updateLesson(Long id, CourseLessonUpdateRequestDto request,
                                              List<MultipartFile> files, List<String> deleteFileUrls) {
        return BaseResponseDto.ok(service.update(id, request, files, deleteFileUrls));
    }

    @Override
    public BaseResponseDto<Long> deleteLesson(Long id) {
        return BaseResponseDto.ok(service.delete(id));
    }

    @Override
    public BaseResponseDto<CourseLessonInfoResponseDto> getLesson(Long id) {
        return BaseResponseDto.ok(service.get(id));
    }

    @Override
    public BaseResponseDto<List<CourseLessonResponseDto>> getLessonsByCourseId(Long courseId) {
        return BaseResponseDto.ok(service.getByCourseId(courseId));
    }
}
