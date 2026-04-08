package uz.asadbek.subcourse.filestorage;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.asadbek.subcourse.filestorage.dto.FileUploadResponse;

@Component
public class FileStorageMapper {

    public FileUploadResponse toResponse(FileStorageEntity entity) {
        var url = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/v1/api/files/")
            .path(entity.getFileKey())
            .toUriString();

        return FileUploadResponse.builder()
            .fileKey(entity.getFileKey())
            .fileName(entity.getStoredName())
            .size(entity.getSize())
            .contentType(entity.getContentType())
            .url(url)
            .build();
    }
}
