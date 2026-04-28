package uz.asadbek.subcourse.course.lesson;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonInfoResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonRequestDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonUpdateRequestDto;

public interface CourseLessonService {

    List<CourseLessonResponseDto> getByCourseId(Long courseId);

    CourseLessonInfoResponseDto get(Long id);

    Long create(CourseLessonRequestDto dto, List<MultipartFile> files);

    Long update(Long id, CourseLessonUpdateRequestDto dto, List<MultipartFile> files, List<String> deleteFileUrls);

    Long delete(Long id);

    Long videoCoursesCount();
}
