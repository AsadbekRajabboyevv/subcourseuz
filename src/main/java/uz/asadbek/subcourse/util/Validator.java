package uz.asadbek.subcourse.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.asadbek.subcourse.course.CourseRepository;
import uz.asadbek.subcourse.course.dto.CourseRequestDto;
import uz.asadbek.subcourse.course.grade.CourseGradeRepository;
import uz.asadbek.subcourse.course.lesson.CourseLessonRepository;
import uz.asadbek.subcourse.science.ScienceRepository;
import uz.asadbek.subcourse.test.TestEntity;
import uz.asadbek.subcourse.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.usertest.UserTestRepository;
import uz.asadbek.subcourse.user.UserRepository;

@Component
@RequiredArgsConstructor
public class Validator {

    private final ScienceRepository scienceRepository;
    private final CourseGradeRepository gradeRepository;
    private final CourseRepository courseRepository;
    private final CourseLessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final UserTestRepository userTestRepository;

    public void validateTest(TestRequestDto dto) {
        Map<String, String> errors = new LinkedHashMap<>();

        validateExists(dto.getScienceId(), scienceRepository::existsById,
            "scienceId", "error.not_found.science", errors);

        validateExists(dto.getGradeId(), gradeRepository::existsById,
            "gradeId", "error.not_found.grade", errors);

        validateRelation(dto.getCourseId(), dto.getScienceId(),
            courseRepository::existsByIdAndScienceId,
            "courseId", "error.invalid.course", errors);

        validateLesson(dto.getLessonId(), dto.getCourseId(), errors);

        throwIfErrors(errors);
    }

    public void validateCourse(CourseRequestDto dto) {
        Map<String, String> errors = new LinkedHashMap<>();

        validateExists(dto.getScienceId(), scienceRepository::existsById,
            "scienceId", "error.not_found.science", errors);

        validateExists(dto.getGradeId(), gradeRepository::existsById,
            "gradeId", "error.not_found.grade", errors);

        throwIfErrors(errors);
    }

    public void validateTestForUpdate(TestEntity test) {
        Map<String, String> errors = new LinkedHashMap<>();

        validateRelation(test.getCourseId(), test.getScienceId(),
            courseRepository::existsByIdAndScienceId,
            "courseId", "error.invalid.course", errors);

        validateLesson(test.getLessonId(), test.getCourseId(), errors);

        throwIfErrors(errors);
    }

    public void validateEnroll(Long userId, Long referenceId,
        Function<Long, Boolean> existsChecker,
        String notFoundMessage) {

        Map<String, String> errors = new LinkedHashMap<>();

        validateExists(userId, userRepository::existsById,
            "userId", "error.not_found.user", errors);

        validateExists(referenceId, existsChecker,
            "referenceId", notFoundMessage, errors);

        if (userId != null && referenceId != null &&
            userTestRepository.existsByIdUserIdAndIdReferenceId(userId, referenceId)) {
            errors.put("enroll", ExceptionUtil.resolveMessage("error.user.already_enrolled"));
        }

        throwIfErrors(errors);
    }

    private void validateExists(Long id,
        Function<Long, Boolean> existsChecker,
        String field,
        String errorKey,
        Map<String, String> errors) {

        if (id == null || !existsChecker.apply(id)) {
            errors.put(field, ExceptionUtil.resolveMessage(errorKey));
        }
    }

    private void validateRelation(Long childId, Long parentId,
        BiFunction<Long, Long, Boolean> relationChecker,
        String field,
        String errorKey,
        Map<String, String> errors) {

        if (childId == null) return;

        if (!relationChecker.apply(childId, parentId)) {
            errors.put(field, ExceptionUtil.resolveMessage(errorKey));
        }
    }

    private void validateLesson(Long lessonId, Long courseId,
        Map<String, String> errors) {

        if (lessonId == null) return;

        if (courseId == null) {
            errors.put("courseId",
                ExceptionUtil.resolveMessage("error.course.required_for_lesson"));
            return;
        }

        if (!lessonRepository.existsByIdAndCourseId(lessonId, courseId)) {
            errors.put("lessonId",
                ExceptionUtil.resolveMessage("error.invalid.lesson"));
        }
    }

    private void throwIfErrors(Map<String, String> errors) {
        if (!errors.isEmpty()) {
            throw ExceptionUtil.validationException("error.validation", errors);
        }
    }
}
