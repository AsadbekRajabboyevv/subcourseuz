package uz.asadbek.subcourse.filestorage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uz.asadbek.subcourse.filestorage.dto.FileUploadResponse;

@Component
public class FileStorageMapper {
    @Value("${app.file-server-url}")
    private String fileServerUrl;

    public FileUploadResponse toResponse(FileStorageEntity entity) {
        return FileUploadResponse.builder()
            .fileKey(entity.getFileKey())
            .fileName(entity.getStoredName())
            .size(entity.getSize())
            .contentType(entity.getContentType())
            .url(fileServerUrl + entity.getFileKey())
            .build();
    }
}
