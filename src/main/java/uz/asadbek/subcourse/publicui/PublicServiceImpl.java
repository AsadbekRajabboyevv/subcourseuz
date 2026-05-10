package uz.asadbek.subcourse.publicui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.asadbek.subcourse.course.CourseService;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;
import uz.asadbek.subcourse.course.grade.CourseGradeService;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;
import uz.asadbek.subcourse.course.lesson.CourseLessonService;
import uz.asadbek.subcourse.publicui.dto.HomePageResponseDto;
import uz.asadbek.subcourse.publicui.dto.HomePageStatsResponseDto;
import uz.asadbek.subcourse.science.ScienceService;
import uz.asadbek.subcourse.science.dto.ScienceResponseDto;
import uz.asadbek.subcourse.test.test.TestService;
import uz.asadbek.subcourse.test.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.test.filter.TestFilter;
import uz.asadbek.subcourse.user.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicServiceImpl implements PublicService {

    private final CourseService courseService;
    private final TestService testService;
    private final UserService userService;
    private final CourseLessonService courseLessonService;
    private final CourseGradeService courseGradeService;
    private final ScienceService scienceService;

    @Override
    public HomePageResponseDto getHomePage() {
        var stats = HomePageStatsResponseDto.builder()
            .coursesCount(courseService.count())
            .testsCount(testService.count())
            .usersCount(userService.count())
            .videoCoursesCount(courseLessonService.videoCoursesCount())
            .build();

        return new HomePageResponseDto(stats, courseGradeService.get(), courseService.getTop(),
            List.of());
    }

    @Override
    public Page<CourseResponseDto> getCourses(CourseFilter filter, Pageable pageable) {
        return courseService.getInfo(pageable, filter);
    }

    @Override
    public CourseInfoResponseDto getCourse(String slug) {
        return courseService.getInfo(slug);
    }

    @Override
    public List<CourseGradeResponseDto> getCourseGrades() {
        return courseGradeService.get();
    }

    @Override
    public List<ScienceResponseDto> getSciences() {
        return scienceService.get();
    }

    @Override
    public Page<TestResponseDto> getTests(TestFilter filter, Pageable pageable) {
        return testService.get(filter, pageable);
    }
}
