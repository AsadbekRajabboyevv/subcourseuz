package uz.asadbek.subcourse.course;

import jakarta.validation.constraints.NotNull;
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

    Page<CourseResponseDto> getMe(Pageable pageable, CourseFilter filter);

    CourseInfoResponseDto getInfo(String slug);

    CourseResponseDto get(String slug);

    Boolean enroll(Long courseId);

    String delete(String slug);

    String update(String slug, CourseUpdateRequestDto request, MultipartFile image);

    String create(MultipartFile image, CourseRequestDto request);

    CourseUpdateRequestDto getUpdateData(String slug);

    Long getIdBySlug(@NotNull String slug);
}
