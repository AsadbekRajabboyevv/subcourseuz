package uz.asadbek.subcourse.publicui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.dto.DurationType;
import uz.asadbek.subcourse.course.filter.CourseFilter;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;
import uz.asadbek.subcourse.publicui.dto.HomePageResponseDto;
import uz.asadbek.subcourse.science.dto.ScienceResponseDto;
import uz.asadbek.subcourse.test.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.test.filter.TestFilter;

@RestController
@RequiredArgsConstructor
public class PublicController implements PublicApi {

    private final PublicService service;

    @Override
    public BaseResponseDto<HomePageResponseDto> getHomePage() {
        return BaseResponseDto.ok(service.getHomePage());
    }

    @Override
    public BaseResponseDto<Page<CourseResponseDto>> getCourses(CourseFilter filter, Pageable pageable) {
        return BaseResponseDto.ok(service.getCourses(filter, pageable));
    }

    @Override
    public BaseResponseDto<CourseInfoResponseDto> getCourse(String slug) {
        return BaseResponseDto.ok(service.getCourse(slug));
    }

    @Override
    public BaseResponseDto<List<CourseGradeResponseDto>> getCourseGrades() {
        return BaseResponseDto.ok(service.getCourseGrades());
    }

    @Override
    public BaseResponseDto<List<ScienceResponseDto>> getSciences() {
        return BaseResponseDto.ok(service.getSciences());
    }

    @Override
    public BaseResponseDto<Page<TestResponseDto>> getTests(TestFilter filter, Pageable pageable) {
        return BaseResponseDto.ok(service.getTests(filter, pageable));
    }

    @Override
    public BaseResponseDto<?> getDurationTypes() {
        return BaseResponseDto.ok(DurationType.values());
    }
}
