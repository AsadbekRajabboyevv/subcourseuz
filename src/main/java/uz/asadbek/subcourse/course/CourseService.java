package uz.asadbek.subcourse.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseRequestDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseUpdateRequestDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;


public interface CourseService {

    Long count();

    Page<CourseResponseDto> getInfo(Pageable pageable, CourseFilter filter);

    CourseInfoResponseDto getInfo(Long id);

    CourseResponseDto get(Long id);

    Boolean enroll(Long courseId);

    Long delete(Long id);

    Long update(Long id, CourseUpdateRequestDto request, MultipartFile image);

    Long create(MultipartFile image, CourseRequestDto request);
}
