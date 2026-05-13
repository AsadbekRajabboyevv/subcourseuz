package uz.asadbek.subcourse.filestorage;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.exception.ValidationException;
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
            throw ExceptionUtil.build(ValidationException.class, "required.file");
        }
        validateExtension(file);
        validateMimeType(file, options);
        validateSize(file, options);
    }

    public void validateVideoInit(String contentType, long totalSize) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (contentType == null || !contentType.startsWith("video/")) {
            errors.put("contentType", ExceptionUtil.resolveMessage("error.content_type.video"));
        }
        if (totalSize <= 0 || totalSize > globalMaxVideoSizeBytes) {
            errors.put("totalSize", ExceptionUtil.resolveMessage("error.size.video", globalMaxVideoSizeBytes));
        }
        if (!errors.isEmpty()) {
            throw ExceptionUtil.validationException("error.validation", errors);
        }
    }

    private void validateSize(MultipartFile file, FileUploadOptions options) {
        long limit = options.maxFileSizeBytes() > 0
            ? options.maxFileSizeBytes()
            : globalMaxSizeBytes;
        if (file.getSize() > limit) {
            log.warn("File size {} exceeds limit {}", file.getSize(), limit);
            throw ExceptionUtil.validationException("file", "file_size_exceeded");
        }
    }

    private void validateMimeType(MultipartFile file, FileUploadOptions options) {
        String mime = file.getContentType();
        if (mime == null) {
            throw ExceptionUtil.validationException("contentType", "file_type_not_allowed");
        }
        String normalizedMime = mime.toLowerCase();
        if (FileUtils.APPLICATION_BLOCKED_MIMES.contains(normalizedMime)
            || !options.isMimeAllowed(normalizedMime)) {
            throw ExceptionUtil.validationException("contentType","file_type_not_allowed");
        }
    }

    private void validateExtension(MultipartFile file) {
        String name = file.getOriginalFilename();
        if (name == null || !name.contains(".")) {
            throw ExceptionUtil.validationException("file", "file_extension_missing");
        }
        String ext = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
        if (FileUtils.APPLICATION_BLOCKED_EXTENSIONS.contains(ext)) {
            throw ExceptionUtil.validationException("file", "file_extension_not_allowed");
        }
    }
}
