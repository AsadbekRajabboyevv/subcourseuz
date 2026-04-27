package uz.asadbek.subcourse.publicui;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;
import uz.asadbek.subcourse.publicui.dto.HomePageResponseDto;
import uz.asadbek.subcourse.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.filter.TestFilter;

@RequestMapping("/v1/api/public")
@Tag(name = "Public", description = "Public")
public interface PublicApi {

    @GetMapping("/home")
    BaseResponseDto<HomePageResponseDto> getHomePage();

    @GetMapping("/courses")
    BaseResponseDto<Page<CourseResponseDto>> getCourses(CourseFilter filter, Pageable pageable);

    @GetMapping("/courses/{id}")
    BaseResponseDto<CourseInfoResponseDto> getCourse(@PathVariable Long id);

    @GetMapping("/course-grades")
    BaseResponseDto<List<CourseGradeResponseDto>> getCourseGrades();

    @GetMapping("/courses/{id}/lessons")
    BaseResponseDto<List<CourseLessonResponseDto>> getCourseLessons(@PathVariable Long id);

    @GetMapping("/tests")
    BaseResponseDto<List<TestResponseDto>> getTests(TestFilter filter, Pageable pageable);

    @GetMapping("/duration-types")
    BaseResponseDto<?> getDurationTypes();
}
