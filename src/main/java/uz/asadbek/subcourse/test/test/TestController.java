package uz.asadbek.subcourse.test.test;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.test.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.test.dto.TestUpdateRequestDto;
import uz.asadbek.subcourse.test.test.filter.TestFilter;

@RestController
@RequiredArgsConstructor
public class TestController implements TestApi {

    private final TestService service;

    @Override
    public BaseResponseDto<Long> create(TestRequestDto request, MultipartFile mainImage,
        MultipartFile[] questionImages, MultipartFile[] optionImages) {
        return BaseResponseDto.ok(
            service.create(request, mainImage, questionImages, optionImages));
    }

    @Override
    public BaseResponseDto<Page<TestResponseDto>> get(Pageable pageable, TestFilter filter) {
        return BaseResponseDto.ok(service.get(filter, pageable));
    }

    @Override
    public BaseResponseDto<TestResponseDto> getInfo(Long id) {
        return BaseResponseDto.ok(service.getInfo(id));
    }

    @Override
    public BaseResponseDto<TestResponseDto> get(Long id) {
        return BaseResponseDto.ok(service.get(id));
    }

    @Override
    public BaseResponseDto<Long> unpublish(Long id) {
        return BaseResponseDto.ok(service.unpublish(id));
    }

    @Override
    public BaseResponseDto<Long> publish(Long id) {
        return BaseResponseDto.ok(service.publish(id));
    }

    @Override
    public BaseResponseDto<Long> update(Long id, TestUpdateRequestDto request, MultipartFile image,
        MultipartFile[] questionImages, MultipartFile[] optionImages) {
        return BaseResponseDto.ok(
            service.update(id, request, image, questionImages, optionImages));
    }
}
