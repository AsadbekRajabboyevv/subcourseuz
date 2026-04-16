package uz.asadbek.subcourse.course;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseRequestDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseUpdateRequestDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;
import uz.asadbek.subcourse.course.grade.CourseGradeService;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeRequestDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeUpdateRequestDto;
import uz.asadbek.subcourse.course.grade.dto.OneCourseGradeResponseDto;

@RestController
@RequiredArgsConstructor
public class CourseController implements CourseApi {

    private final CourseGradeService courseGradeService;
    private final CourseService service;

    @Override
    public BaseResponseDto<List<CourseGradeResponseDto>> getCourseGrades() {
        return BaseResponseDto.ok(courseGradeService.get());
    }

    @Override
    public BaseResponseDto<CourseGradeResponseDto> saveCourseGrade(CourseGradeRequestDto request) {
        return BaseResponseDto.ok(courseGradeService.create(request));
    }

    @Override
    public BaseResponseDto<CourseGradeResponseDto> updateCourseGrade(Long id,
        CourseGradeUpdateRequestDto request) {
        return BaseResponseDto.ok(courseGradeService.update(id, request));
    }

    @Override
    public BaseResponseDto<?> deleteCourseGrade(Long id) {
        courseGradeService.delete(id);
        return BaseResponseDto.ok("Successfully deleted");
    }

    @Override
    public BaseResponseDto<OneCourseGradeResponseDto> getCourseGrade(Long id) {
        return BaseResponseDto.ok(courseGradeService.get(id));
    }

    @Override
    public BaseResponseDto<Page<CourseResponseDto>> get(CourseFilter filter,
        Pageable pageable) {
        return BaseResponseDto.ok(service.getInfo(pageable, filter));
    }

    @Override
    public BaseResponseDto<Page<CourseResponseDto>> getMe(CourseFilter filter, Pageable pageable) {
        return BaseResponseDto.ok(service.getMe(pageable, filter));
    }

    @Override
    public BaseResponseDto<CourseInfoResponseDto> get(Long id) {
        return BaseResponseDto.ok(service.getInfo(id));
    }

    @Override
    public BaseResponseDto<Boolean> enroll(Long id) {
        return BaseResponseDto.ok(service.enroll(id));
    }

    @Override
    public BaseResponseDto<Long> create(CourseRequestDto request, MultipartFile image) {
        return BaseResponseDto.ok(service.create(image, request));
    }

    @Override
    public BaseResponseDto<Long> update(Long id, MultipartFile image,
        CourseUpdateRequestDto request) {
        return BaseResponseDto.ok(service.update(id, request, image));
    }

    @Override
    public BaseResponseDto<Long> delete(Long id) {
        return BaseResponseDto.ok(service.delete(id));
    }

}
