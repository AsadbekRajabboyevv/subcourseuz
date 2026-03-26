package uz.asadbek.subcourse.filestorage;

import org.springframework.stereotype.Component;
import uz.asadbek.subcourse.filestorage.dto.FileUploadResponse;

@Component
public class FileStorageMapper {

    public FileUploadResponse toResponse(FileStorageEntity entity) {
        return FileUploadResponse.builder()
            .fileKey(entity.getFileKey())
            .fileName(entity.getStoredName())
            .url(entity.getUrl())
            .size(entity.getSize())
            .contentType(entity.getContentType())
            .build();
    }
}
