package uz.asadbek.subcourse.filestorage;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.filestorage.dto.FileResource;
import uz.asadbek.subcourse.filestorage.dto.FileUploadOptions;
import uz.asadbek.subcourse.filestorage.dto.FileUploadResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageRepository repository;
    private final FileStorageMapper mapper;

    private final Path root = Paths.get("uploads").toAbsolutePath().normalize();

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(root);
    }

    @Override
    public FileUploadResponse upload(MultipartFile file) {
        return upload(file, FileUploadOptions.builder().build());
    }

    @Override
    public FileUploadResponse upload(MultipartFile file, FileUploadOptions options) {

        validate(file);

        String fileKey = UUID.randomUUID().toString();
        String extension = getExtension(file.getOriginalFilename());
        String storedName = fileKey + "." + extension;

        try {
            Path target = root.resolve(storedName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            FileStorageEntity entity = FileStorageEntity.builder()
                .fileKey(fileKey)
                .originalName(file.getOriginalFilename())
                .storedName(storedName)
                .path(options.getFolder())
                .url("/files/" + fileKey)
                .size(file.getSize())
                .contentType(file.getContentType())
                .isPublic(options.isPublicAccess())
                .build();

            repository.save(entity);

            return mapper.toResponse(entity);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed");
        }
    }

    @Override
    public void delete(String fileKey) {

        FileStorageEntity entity = repository.findByFileKey(fileKey)
            .orElseThrow(() -> new RuntimeException("File not found"));

        try {
            Path filePath = root.resolve(entity.getStoredName());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("File delete failed");
        }

        repository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileResource> get(String fileKey) {

        return repository.findByFileKey(fileKey)
            .map(entity -> {
                try {
                    Path filePath = root.resolve(entity.getStoredName());

                    return FileResource.builder()
                        .fileKey(entity.getFileKey())
                        .fileName(entity.getStoredName())
                        .contentType(entity.getContentType())
                        .size(entity.getSize())
                        .inputStream(Files.newInputStream(filePath))
                        .build();

                } catch (IOException e) {
                    throw new RuntimeException("File read error");
                }
            });
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
    }

    private String getExtension(String name) {
        int i = name.lastIndexOf('.');
        return i > 0 ? name.substring(i + 1) : "";
    }
}
