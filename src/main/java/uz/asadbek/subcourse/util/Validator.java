package uz.asadbek.subcourse.util;

import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.asadbek.subcourse.course.CourseRepository;
import uz.asadbek.subcourse.course.grade.CourseGradeRepository;
import uz.asadbek.subcourse.course.lesson.CourseLessonRepository;
import uz.asadbek.subcourse.science.ScienceRepository;
import uz.asadbek.subcourse.test.TestEntity;
import uz.asadbek.subcourse.test.TestRepository;
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
        validateExists(dto.getScienceId(), scienceRepository::existsById, "science_not_found");
        validateExists(dto.getGradeId(), gradeRepository::existsById, "grade_not_found");

        validateRelation(dto.getCourseId(), dto.getScienceId(),
            courseRepository::existsByIdAndScienceId, "invalid_course");

        validateLesson(dto.getLessonId(), dto.getCourseId());
    }

    public void validateTestForUpdate(TestEntity test) {
        validateRelation(test.getCourseId(), test.getScienceId(),
            courseRepository::existsByIdAndScienceId, "invalid_course");

        validateLesson(test.getLessonId(), test.getCourseId());
    }

    public void validateEnroll(Long userId, Long referenceId, Function<Long, Boolean> existsChecker,
        String notFoundMessage) {
        validateExists(userId, userRepository::existsById, "user_not_found");
        validateExists(referenceId, existsChecker, notFoundMessage);

        if (userTestRepository.existsByIdUserIdAndIdReferenceId(userId, referenceId)) {
            throw ExceptionUtil.badRequestException("user_already_enrolled");
        }
    }

    private void validateExists(Long id, Function<Long, Boolean> existsChecker,
        String errorMessage) {
        if (!existsChecker.apply(id)) {
            throw ExceptionUtil.badRequestException(errorMessage);
        }
    }

    private void validateRelation(Long childId, Long parentId,
        BiFunction<Long, Long, Boolean> relationChecker, String errorMessage) {
        if (childId == null) {
            return;
        }

        if (!relationChecker.apply(childId, parentId)) {
            throw ExceptionUtil.badRequestException(errorMessage);
        }
    }

    private void validateLesson(Long lessonId, Long courseId) {
        if (lessonId == null) {
            return;
        }

        if (courseId == null) {
            throw ExceptionUtil.badRequestException("course_required_for_lesson");
        }

        if (!lessonRepository.existsByIdAndCourseId(lessonId, courseId)) {
            throw ExceptionUtil.badRequestException("invalid_lesson");
        }
    }
}
