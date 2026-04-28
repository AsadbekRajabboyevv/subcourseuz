package uz.asadbek.subcourse.publicui;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;
import uz.asadbek.subcourse.publicui.dto.HomePageResponseDto;
import uz.asadbek.subcourse.science.dto.ScienceResponseDto;

public interface PublicService {

    HomePageResponseDto getHomePage();

    Page<CourseResponseDto> getCourses(CourseFilter filter, Pageable pageable);

    CourseInfoResponseDto getCourse(String slug);

    List<CourseLessonResponseDto> getCourseLessons();

    List<CourseGradeResponseDto> getCourseGrades();

    List<ScienceResponseDto> getSciences();

}
