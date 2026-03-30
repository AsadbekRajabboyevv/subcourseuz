package uz.asadbek.subcourse.test.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.asadbek.subcourse.course.CourseRepository;
import uz.asadbek.subcourse.course.grade.CourseGradeRepository;
import uz.asadbek.subcourse.course.lesson.CourseLessonRepository;
import uz.asadbek.subcourse.science.ScienceRepository;
import uz.asadbek.subcourse.test.TestEntity;
import uz.asadbek.subcourse.test.dto.TestRequestDto;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Component
@RequiredArgsConstructor
public class TestValidator {

    private final ScienceRepository scienceRepository;
    private final CourseGradeRepository gradeRepository;
    private final CourseRepository courseRepository;
    private final CourseLessonRepository lessonRepository;

    public void validate(TestRequestDto dto) {

        validateRequired(dto);
        validateCourse(dto);
        validateLesson(dto);
    }

    private void validateRequired(TestRequestDto dto) {

        if (!scienceRepository.existsById(dto.getScienceId())) {
            throw ExceptionUtil.badRequestException("science_not_found");
        }

        if (!gradeRepository.existsById(dto.getGradeId())) {
            throw ExceptionUtil.badRequestException("grade_not_found");
        }
    }

    private void validateCourse(TestRequestDto dto) {

        if (dto.getCourseId() == null) {
            return;
        }

        boolean exists = courseRepository.existsByIdAndScienceId(
            dto.getCourseId(), dto.getScienceId()
        );

        if (!exists) {
            throw ExceptionUtil.badRequestException("invalid_course");
        }
    }

    private void validateLesson(TestRequestDto dto) {

        if (dto.getLessonId() == null) {
            return;
        }

        if (dto.getCourseId() == null) {
            throw ExceptionUtil.badRequestException("course_required_for_lesson");
        }

        boolean exists = lessonRepository.existsByIdAndCourseId(
            dto.getLessonId(),
            dto.getCourseId()
        );

        if (!exists) {
            throw ExceptionUtil.badRequestException("invalid_lesson");
        }
    }

    public void validateForUpdate(TestEntity test) {

        if (test.getCourseId() != null) {
            boolean valid = courseRepository.existsByIdAndScienceId(
                test.getCourseId(),
                test.getScienceId()
            );

            if (!valid) {
                throw ExceptionUtil.badRequestException("invalid_course");
            }
        }

        if (test.getLessonId() != null) {

            if (test.getCourseId() == null) {
                throw ExceptionUtil.badRequestException("course_required_for_lesson");
            }

            boolean valid = lessonRepository.existsByIdAndCourseId(
                test.getLessonId(),
                test.getCourseId()
            );

            if (!valid) {
                throw ExceptionUtil.badRequestException("invalid_lesson");
            }
        }
    }
}
