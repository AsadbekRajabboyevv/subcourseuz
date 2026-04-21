package uz.asadbek.subcourse.science;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.filestorage.FileStorageService;
import uz.asadbek.subcourse.filestorage.dto.FileUploadOptions;
import uz.asadbek.subcourse.science.dto.OneScienceResponseDto;
import uz.asadbek.subcourse.science.dto.ScienceRequestDto;
import uz.asadbek.subcourse.science.dto.ScienceResponseDto;
import uz.asadbek.subcourse.science.dto.ScienceUpdateRequestDto;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.LangUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScienceServiceImpl implements ScienceService {

    private final ScienceRepository repository;
    private final ScienceMapper mapper;
    private final FileStorageService fileStorageService;

    @Override
    public List<ScienceResponseDto> get() {
        var lang = "uz";
        return repository.get(lang);
    }

    @Override
    public OneScienceResponseDto get(Long id) {
        return repository.get(id);
    }

    @Override
    @Transactional
    public ScienceResponseDto create(ScienceRequestDto request, MultipartFile image) {
        var entity = mapper.toEntity(request);
        if (image != null && !image.isEmpty()) {
            var path = fileStorageService.upload(image,
                new FileUploadOptions().setScienceImages()).url();
            entity.setImagePath(path);
        }
        var saved = repository.save(entity);
        return getResponse(saved);
    }

    @Override
    @Transactional
    public ScienceResponseDto update(Long id, ScienceUpdateRequestDto request,
        MultipartFile image) {
        var science = repository.findById(id).orElseThrow(()-> ExceptionUtil.notFoundException("science_not_found"));
        mapper.update(science, request);
        if (image != null && !image.isEmpty()) {
            var path = fileStorageService.upload(image,
                new FileUploadOptions().setScienceImages()).url();
            science.setImagePath(path);
        }
        var saved = repository.save(science);
        return getResponse(saved);
    }

    @Override
    @Transactional
    public Long delete(Long id) {
        repository.deleteById(id);
        return id;
    }

    private static ScienceResponseDto getResponse(ScienceEntity saved) {
        return new ScienceResponseDto(
            saved.getId(),
            LangUtils.getName(saved.getName()),
            LangUtils.getDescription(saved.getDescription()),
            saved.getImagePath());
    }
}
