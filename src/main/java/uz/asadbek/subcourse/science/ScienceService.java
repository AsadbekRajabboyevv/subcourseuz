package uz.asadbek.subcourse.science;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.science.dto.OneScienceResponseDto;
import uz.asadbek.subcourse.science.dto.ScienceRequestDto;
import uz.asadbek.subcourse.science.dto.ScienceResponseDto;
import uz.asadbek.subcourse.science.dto.ScienceUpdateRequestDto;

public interface ScienceService {

    List<ScienceResponseDto> get();

    OneScienceResponseDto get(Long id);

    ScienceResponseDto create(ScienceRequestDto request, MultipartFile image);

    ScienceResponseDto update(Long id, ScienceUpdateRequestDto request, MultipartFile image);

    Long delete(Long id);
}
