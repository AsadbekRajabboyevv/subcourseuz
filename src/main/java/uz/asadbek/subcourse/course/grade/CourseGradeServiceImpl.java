package uz.asadbek.subcourse.course.grade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeRequestDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeUpdateRequestDto;
import uz.asadbek.subcourse.course.grade.dto.OneCourseGradeResponseDto;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;
import uz.asadbek.subcourse.util.LangUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseGradeServiceImpl implements CourseGradeService {

    private final CourseGradeRepository repository;
    private final CourseGradeMapper mapper;

    @Override
    public List<CourseGradeResponseDto> get() {
        return repository.get(LangUtils.currentLang());
    }

    @Override
    public OneCourseGradeResponseDto get(Long id) {
        return repository.get(id);
    }

    @Override
    @Transactional
    public CourseGradeResponseDto create(CourseGradeRequestDto request) {

        CourseGradeEntity entity = mapper.toEntity(request);
        CourseGradeEntity saved = repository.save(entity);

        return getCourseGradeResponseDto(saved);
    }

    @Override
    @Transactional
    public CourseGradeResponseDto update(Long id, CourseGradeUpdateRequestDto request) {
        var courseGrade = repository.findById(id)
            .orElseThrow(() -> ExceptionUtil.notFoundException("course_grade_not_found"));
        mapper.update(courseGrade, request);
        var saved = repository.save(courseGrade);
        return getCourseGradeResponseDto(saved);
    }

    private static CourseGradeResponseDto getCourseGradeResponseDto(CourseGradeEntity saved) {
        return new CourseGradeResponseDto(
            saved.getId(),
            LangUtils.getName(saved.getName()),
            LangUtils.getDescription(saved.getDescription())
        );
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
