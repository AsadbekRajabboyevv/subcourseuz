package uz.asadbek.subcourse.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;


public interface CourseService {

    Long count();

    Page<CourseResponseDto> getInfo(Pageable pageable, CourseFilter filter);

    CourseInfoResponseDto getInfo(Long id);

    CourseResponseDto get(Long id);

    Boolean enroll(Long courseId);
}
