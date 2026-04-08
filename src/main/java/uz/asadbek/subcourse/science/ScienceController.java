package uz.asadbek.subcourse.science;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.science.dto.OneScienceResponseDto;
import uz.asadbek.subcourse.science.dto.ScienceRequestDto;
import uz.asadbek.subcourse.science.dto.ScienceResponseDto;
import uz.asadbek.subcourse.science.dto.ScienceUpdateRequestDto;

@RestController
@RequiredArgsConstructor
public class ScienceController implements ScienceApi {

    private final ScienceService service;

    @Override
    public BaseResponseDto<List<ScienceResponseDto>> get() {
        return BaseResponseDto.ok(service.get());
    }

    @Override
    public BaseResponseDto<OneScienceResponseDto> get(Long id) {
        return BaseResponseDto.ok(service.get(id));
    }

    @Override
    public BaseResponseDto<ScienceResponseDto> create(ScienceRequestDto request,
        MultipartFile image) {
        return BaseResponseDto.ok(service.create(request, image));
    }

    @Override
    public BaseResponseDto<ScienceResponseDto> update(Long id, ScienceUpdateRequestDto request,
        MultipartFile image) {
        return BaseResponseDto.ok(service.update(id, request, image));
    }

    @Override
    public BaseResponseDto<Long> delete(Long id) {
        return BaseResponseDto.ok(service.delete(id));
    }
}
