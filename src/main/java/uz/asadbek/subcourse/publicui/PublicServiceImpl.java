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
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;
import uz.asadbek.subcourse.publicui.dto.HomePageResponseDto;
import uz.asadbek.subcourse.publicui.dto.StatsDto;
import uz.asadbek.subcourse.test.TestService;
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

    @Override
    public HomePageResponseDto getHomePage() {
        var stats = StatsDto.builder()
            .coursesCount(courseService.count())
            .testsCount(testService.count())
            .usersCount(userService.count())
            .videoCoursesCount(courseLessonService.videoCoursesCount())
            .build();

        return new HomePageResponseDto(stats, List.of(), List.of(), List.of());
    }

    @Override
    public Page<CourseResponseDto> getCourses(CourseFilter filter, Pageable pageable) {
        return courseService.getInfo(pageable, filter);
    }

    @Override
    public CourseInfoResponseDto getCourse(Long id) {
        return courseService.getInfo(id);
    }

    @Override
    public List<CourseLessonResponseDto> getCourseLessons() {
        return List.of();
    }

    @Override
    public List<CourseGradeResponseDto> getCourseGrades() {
        return courseGradeService.get();
    }
}
