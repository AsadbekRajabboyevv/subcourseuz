package uz.asadbek.subcourse.course;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;
import uz.asadbek.subcourse.course.usercourse.UserCourse;
import uz.asadbek.subcourse.course.usercourse.UserCourseId;
import uz.asadbek.subcourse.course.usercourse.UserCourseRepository;
import uz.asadbek.subcourse.user.UserRepository;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;
import uz.asadbek.subcourse.util.LangUtils;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserCourseRepository userCourseRepository;

    @Override
    public Long count() {
        return courseRepository.count();
    }

    @Override
    public Page<CourseResponseDto> get(Pageable pageable, CourseFilter filter) {

        return courseRepository.get(pageable, filter, LangUtils.currentLang());
    }

    @Override
    public CourseInfoResponseDto get(Long id) {
        return courseRepository.get(id, LangUtils.currentLang());
    }

    @Override
    public Boolean enroll(Long courseId) {
        var userId = JwtUtil.getCurrentUser().getId();

        if (!userRepository.existsById(userId)) {
            log.error("User not found: {}", userId);
            throw ExceptionUtil.notFoundException("user_not_found");
        }

        if (!courseRepository.existsById(courseId)) {
            log.error("Course not found: {}", courseId);
            throw ExceptionUtil.notFoundException("course_not_found");
        }

        if (userCourseRepository.existsByIdUserIdAndIdCourseId(userId, courseId)) {
            log.error("User already enrolled to course: {}", courseId);
            throw ExceptionUtil.badRequestException("user_already_enrolled");
        }

        var uc = new UserCourse();
        uc.setId(new UserCourseId(userId, courseId));
        uc.setJoinedAt(LocalDateTime.now());

        userCourseRepository.save(uc);
        return true;
    }


}
