package uz.asadbek.subcourse.filestorage;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.filestorage.dto.*;

public interface FileStorageApi {

    ResponseEntity<FileUploadResponse> uploadFile(MultipartFile file);

    ResponseEntity<Resource> getFile(String fileKey);

    ResponseEntity<FileMetadata> metadata(String fileKey);

    ResponseEntity<Void> softDelete(String fileKey);

    ResponseEntity<Void> hardDelete(String fileKey);

    ResponseEntity<Void> restore(String fileKey);

    ResponseEntity<VideoUploadSession> initVideoUpload(VideoUploadInitRequestDto request);

    ResponseEntity<Void> uploadChunk(String sessionId, int chunkIndex, MultipartFile chunk);

    ResponseEntity<FileUploadResponse> completeVideoUpload(String sessionId);

    ResponseEntity<Void> abortVideoUpload(String sessionId);

    ResponseEntity<Resource> streamVideo(String fileKey, String rangeHeader);
}
