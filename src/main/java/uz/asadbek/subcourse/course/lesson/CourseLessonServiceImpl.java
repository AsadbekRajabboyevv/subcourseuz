package uz.asadbek.subcourse.course.lesson;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonInfoResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonRequestDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonUpdateRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseLessonServiceImpl implements CourseLessonService {

    private final CourseLessonRepository repository;
    private final CourseLessonMapper mapper;

    @Override
    public List<CourseLessonResponseDto> getByCourseId(Long courseId) {
        return repository.getByCourseId(courseId);
    }

    @Override
    public CourseLessonInfoResponseDto get(Long id) {
        return repository.get(id);
    }

    @Override
    public CourseLessonResponseDto create(Long courseId, CourseLessonRequestDto dto) {
        return null;
    }

    @Override
    public CourseLessonInfoResponseDto update(Long id, CourseLessonUpdateRequestDto dto) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public Long videoCoursesCount() {
        return 0L;
    }

}
