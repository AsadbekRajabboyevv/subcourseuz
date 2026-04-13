package uz.asadbek.subcourse.filestorage;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.filestorage.dto.FileResource;
import uz.asadbek.subcourse.filestorage.dto.FileUploadOptions;
import uz.asadbek.subcourse.filestorage.dto.FileUploadResponse;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageRepository repository;
    private final FileStorageMapper mapper;
    @Value("${app.file-server-url}")
    private String fileServerUrl;

    private final Path root = Paths.get("uploads").toAbsolutePath().normalize();

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(root);
    }

    @Override
    @Transactional
    public FileUploadResponse upload(MultipartFile file, FileUploadOptions options) {

        validate(file);

        String fileKey = UUID.randomUUID().toString();
        String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        String storedName = STR."\{fileKey}.\{extension}";

        try {
            Path target = root.resolve(storedName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            FileStorageEntity entity = FileStorageEntity.builder()
                .fileKey(fileKey)
                .originalName(file.getOriginalFilename())
                .storedName(storedName)
                .path(options.getFolder())
                .size(file.getSize())
                .contentType(file.getContentType())
                .isPublic(options.isPublicAccess())
                .build();

            repository.save(entity);

            return mapper.toResponse(entity);

        } catch (IOException e) {
            throw ExceptionUtil.badRequestException("file_upload_error");
        }
    }

    @Override
    public void delete(String fileUrl) {
        var fileKey = fileUrl.substring(fileServerUrl.length());
        FileStorageEntity entity = repository.findByFileKey(fileKey)
            .orElseThrow(() -> ExceptionUtil.notFoundException("file_not_found"));

        try {
            Path filePath = root.resolve(entity.getStoredName());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Error deleting file: {}", ExceptionUtils.getStackTrace(e));
            throw ExceptionUtil.badRequestException("file_delete_error");
        }

        repository.delete(entity);
    }

    @Override
    public Optional<FileResource> get(String fileKey) {

        return repository.findByFileKey(fileKey)
            .map(entity -> {
                try {
                    Path filePath = root.resolve(entity.getStoredName()).normalize();

                    Resource resource = new UrlResource(filePath.toUri());

                    if (!resource.exists()) {
                        throw ExceptionUtil.notFoundException("file_not_found");
                    }

                    return FileResource.builder()
                        .fileKey(entity.getFileKey())
                        .fileName(entity.getStoredName())
                        .contentType(entity.getContentType())
                        .size(entity.getSize())
                        .resource(resource)
                        .build();

                } catch (MalformedURLException e) {
                    log.error("Error reading file", e);
                    throw ExceptionUtil.badRequestException("file_read_error");
                }
            });
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw ExceptionUtil.badRequestException("file_required");
        }
    }

    private String getExtension(String name) {
        int i = name.lastIndexOf('.');
        return i > 0 ? name.substring(i + 1) : "";
    }
}
