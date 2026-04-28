package uz.asadbek.subcourse.course;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseRequestDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseUpdateRequestDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;

@RestController
@RequiredArgsConstructor
public class CourseController implements CourseApi {

    private final CourseService service;

    @Override
    public BaseResponseDto<Page<CourseResponseDto>> get(CourseFilter filter,
        Pageable pageable) {
        return BaseResponseDto.ok(service.getInfo(pageable, filter));
    }

    @Override
    public BaseResponseDto<Page<CourseResponseDto>> getMe(CourseFilter filter, Pageable pageable) {
        return BaseResponseDto.ok(service.getMe(pageable, filter));
    }

    @Override
    public BaseResponseDto<CourseInfoResponseDto> get(String slug) {
        return BaseResponseDto.ok(service.getInfo(slug));
    }

    @Override
    public BaseResponseDto<String> create(CourseRequestDto request, MultipartFile image) {
        return BaseResponseDto.ok(service.create(image, request));
    }

    @Override
    public BaseResponseDto<CourseUpdateRequestDto> update(String slug) {
        return BaseResponseDto.ok(service.getUpdateData(slug));
    }

    @Override
    public BaseResponseDto<String> update(String slug, MultipartFile image,
        CourseUpdateRequestDto request) {
        return BaseResponseDto.ok(service.update(slug, request, image));
    }

    @Override
    public BaseResponseDto<String> delete(String slug) {
        return BaseResponseDto.ok(service.delete(slug));
    }

}
