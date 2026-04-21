package uz.asadbek.subcourse.filestorage;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.filestorage.dto.FileUploadOptions;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Slf4j
@Component
public class FileStorageValidator {

    @Value("${app.file.storage.max-size-bytes:52428800}")
    private long globalMaxSizeBytes;

    @Value("${app.file.storage.max-video-size-bytes:5368709120}")
    private long globalMaxVideoSizeBytes;

    public void validate(MultipartFile file, FileUploadOptions options) {
        if (file == null || file.isEmpty()) {
            throw ExceptionUtil.badRequestException("file_required");
        }
        validateExtension(file);
        validateMimeType(file, options);
        validateSize(file, options);
    }

    public void validateVideoInit(String contentType, long totalSize) {
        if (contentType == null || !contentType.startsWith("video/")) {
            throw ExceptionUtil.badRequestException("video_content_type_invalid");
        }
        if (totalSize <= 0 || totalSize > globalMaxVideoSizeBytes) {
            throw ExceptionUtil.badRequestException("video_size_invalid");
        }
    }

    private void validateSize(MultipartFile file, FileUploadOptions options) {
        long limit = options.maxFileSizeBytes() > 0
            ? options.maxFileSizeBytes()
            : globalMaxSizeBytes;
        if (file.getSize() > limit) {
            log.warn("File size {} exceeds limit {}", file.getSize(), limit);
            throw ExceptionUtil.badRequestException("file_size_exceeded");
        }
    }

    private void validateMimeType(MultipartFile file, FileUploadOptions options) {
        String mime = file.getContentType();
        if (mime == null || FileUtils.APPLICATION_BLOCKED_MIMES.contains(mime.toLowerCase())) {
            throw ExceptionUtil.badRequestException("file_type_not_allowed");
        }
        if (options.allowedMimeTypes() != null) {
            boolean allowed = Arrays.stream(options.allowedMimeTypes().split(","))
                .map(String::trim)
                .anyMatch(t -> t.equals(mime) || mime.startsWith(t.replace("*", "")));
            if (!allowed) {
                throw ExceptionUtil.badRequestException("file_type_not_allowed");
            }
        }
    }

    private void validateExtension(MultipartFile file) {
        String name = file.getOriginalFilename();
        if (name == null || !name.contains(".")) {
            throw ExceptionUtil.badRequestException("file_extension_missing");
        }
        String ext = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
        if (FileUtils.APPLICATION_BLOCKED_EXTENSIONS.contains(ext)) {
            throw ExceptionUtil.badRequestException("file_extension_not_allowed");
        }
    }
}
