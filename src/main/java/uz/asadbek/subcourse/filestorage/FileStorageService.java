package uz.asadbek.subcourse.filestorage;

import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.filestorage.dto.FileResource;
import uz.asadbek.subcourse.filestorage.dto.FileUploadOptions;
import uz.asadbek.subcourse.filestorage.dto.FileUploadResponse;

public interface FileStorageService {

    FileUploadResponse upload(MultipartFile file);

    FileUploadResponse upload(MultipartFile file, FileUploadOptions options);

    void delete(String fileKey);

    Optional<FileResource> get(String fileKey);
}
