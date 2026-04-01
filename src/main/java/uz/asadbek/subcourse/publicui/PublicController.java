package uz.asadbek.subcourse.publicui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;
import uz.asadbek.subcourse.publicui.dto.HomePageResponseDto;
import uz.asadbek.subcourse.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.filter.TestFilter;

@RestController
@RequiredArgsConstructor
public class PublicController implements PublicApi {

    @Override
    public BaseResponseDto<HomePageResponseDto> getHomePage() {
        return null;
    }

    @Override
    public BaseResponseDto<Page<CourseGradeResponseDto>> getCourses(CourseFilter filter, Pageable pageable) {
        return null;
    }

    @Override
    public BaseResponseDto<CourseResponseDto> getCourse(Long id) {
        return null;
    }

    @Override
    public BaseResponseDto<List<CourseGradeResponseDto>> getCourseGrades() {
        return null;
    }

    @Override
    public BaseResponseDto<List<CourseLessonResponseDto>> getCourseLessons(Long id) {
        return null;
    }

    @Override
    public BaseResponseDto<List<TestResponseDto>> getTests(TestFilter filter, Pageable pageable) {
        return null;
    }
}
