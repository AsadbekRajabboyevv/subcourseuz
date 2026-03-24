package uz.asadbek.subcourse.course.lesson;

import java.util.List;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonInfoResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonRequestDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonUpdateRequestDto;

public interface CourseLessonService {

    List<CourseLessonResponseDto> getByCourseId(Long courseId);
    CourseLessonInfoResponseDto get(Long id);
    CourseLessonResponseDto create(Long courseId, CourseLessonRequestDto dto);
    CourseLessonInfoResponseDto update(Long id, CourseLessonUpdateRequestDto dto);
    Boolean delete(Long id);

    Long videoCoursesCount();
}
