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
import uz.asadbek.subcourse.course.usercourse.UserCourseEntity;
import uz.asadbek.subcourse.util.Validator;
import uz.asadbek.subcourse.util.embedded.UserPurchaseId;
import uz.asadbek.subcourse.course.usercourse.UserCourseRepository;
import uz.asadbek.subcourse.util.JwtUtil;
import uz.asadbek.subcourse.util.LangUtils;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;
    private final UserCourseRepository userCourseRepository;
    private final Validator validator;

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public Page<CourseResponseDto> getInfo(Pageable pageable, CourseFilter filter) {
        return repository.get(pageable, filter, LangUtils.currentLang());
    }

    @Override
    public CourseInfoResponseDto getInfo(Long id) {
        return repository.get(id, LangUtils.currentLang());
    }

    @Override
    public CourseResponseDto get(Long id) {
        return null;
    }

    @Override
    public Boolean enroll(Long courseId, Long userId) {
        validator.validateEnroll(userId, courseId, repository::existsById, "course_not_found");
        var uc = new UserCourseEntity();
        uc.setId(new UserPurchaseId(userId, courseId, LocalDateTime.now()));

        userCourseRepository.save(uc);
        return true;
    }

}
