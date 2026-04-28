package uz.asadbek.subcourse.filestorage;

import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.filestorage.dto.*;

public interface FileStorageService {

    // ── Regular file ──────────────────────────────────────────────────────────
    FileUploadResponse upload(MultipartFile file, FileUploadOptions options);

    Optional<FileResource> get(String fileKey);

    FileMetadata getMetadata(String fileKey);

    void softDelete(String fileKey);

    void hardDelete(String fileKey);

    void restore(String fileKey);

    void enforceQuota(Long ownerId, long incomingBytes);

    // ── Chunked video upload ──────────────────────────────────────────────────
    VideoUploadSession initVideoUpload(VideoUploadInitRequestDto request);

    void uploadChunk(String sessionId, int chunkIndex, MultipartFile chunk);

    FileUploadResponse completeVideoUpload(String sessionId);

    void abortVideoUpload(String sessionId);

    // ── Video streaming ───────────────────────────────────────────────────────
    VideoStreamResource streamVideo(String fileKey, String rangeHeader);
}
