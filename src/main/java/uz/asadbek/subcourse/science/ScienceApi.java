package uz.asadbek.subcourse.science;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.science.dto.OneScienceResponseDto;
import uz.asadbek.subcourse.science.dto.ScienceRequestDto;
import uz.asadbek.subcourse.science.dto.ScienceResponseDto;
import uz.asadbek.subcourse.science.dto.ScienceUpdateRequestDto;

@RequestMapping("/v1/api/sciences")
public interface ScienceApi {

    @GetMapping
    BaseResponseDto<List<ScienceResponseDto>> get();

    @GetMapping("/{id}")
    BaseResponseDto<OneScienceResponseDto> get(@PathVariable Long id);

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BaseResponseDto<ScienceResponseDto> create(
        @RequestPart ScienceRequestDto request,
        @RequestPart(required = false) MultipartFile image);

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BaseResponseDto<ScienceResponseDto> update(
        @PathVariable Long id,
        @RequestPart(required = false) ScienceUpdateRequestDto request,
        @RequestPart(required = false) MultipartFile image);

    @DeleteMapping("/{id}")
    BaseResponseDto<Long> delete(@PathVariable Long id);
}
