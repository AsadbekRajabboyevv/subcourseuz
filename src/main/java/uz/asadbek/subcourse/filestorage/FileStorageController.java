package uz.asadbek.subcourse.filestorage;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.subcourse.exception.NotFoundException;
import uz.asadbek.subcourse.util.ExceptionUtil;

import java.net.URI;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.filestorage.dto.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "File Storage API", description = "API for managing files")
public class FileStorageController implements FileStorageApi {

    private final FileStorageService fileStorageService;

    @Override
    @PostMapping
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(fileStorageService.upload(file, FileUploadOptions.OTHER));
    }

    @Override
    @GetMapping("/{fileKey}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileKey) {
        var file = fileStorageService.get(fileKey)
            .orElseThrow(() -> ExceptionUtil.build(NotFoundException.class, "error.not_found.file"));

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(file.contentType()))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + file.fileName() + "\"")
            .body(file.resource());
    }

    @Override
    @GetMapping("/{fileKey}/metadata")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileMetadata> metadata(@PathVariable String fileKey) {
        return ResponseEntity.ok(fileStorageService.getMetadata(fileKey));
    }

    @Override
    @DeleteMapping("/{fileKey}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> softDelete(@PathVariable String fileKey) {
        fileStorageService.softDelete(fileKey);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{fileKey}/hard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> hardDelete(@PathVariable String fileKey) {
        fileStorageService.hardDelete(fileKey);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/{fileKey}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> restore(@PathVariable String fileKey) {
        fileStorageService.restore(fileKey);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/videos/init")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VideoUploadSession> initVideoUpload(
        @RequestBody VideoUploadInitRequestDto request
    ) {
        return ResponseEntity.ok(fileStorageService.initVideoUpload(request));
    }

    @Override
    @PostMapping(value = "/videos/chunk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> uploadChunk(
        @RequestParam String sessionId,
        @RequestParam int chunkIndex,
        @RequestPart("chunk") MultipartFile chunk
    ) {
        fileStorageService.uploadChunk(sessionId, chunkIndex, chunk);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/videos/complete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileUploadResponse> completeVideoUpload(
        @RequestParam String sessionId
    ) {
        FileUploadResponse response = fileStorageService.completeVideoUpload(sessionId);
        return ResponseEntity
            .created(URI.create("/v1/api/files/" + response.getFileKey()))
            .body(response);
    }

    @Override
    @DeleteMapping("/videos/abort")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> abortVideoUpload(@RequestParam String sessionId) {
        fileStorageService.abortVideoUpload(sessionId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/videos/{fileKey}")
    public ResponseEntity<Resource> streamVideo(
        @PathVariable String fileKey,
        @RequestHeader(value = HttpHeaders.RANGE, required = false) String rangeHeader
    ) {
        VideoStreamResource vsr = fileStorageService.streamVideo(fileKey, rangeHeader);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(vsr.contentType()));
        headers.setContentLength(vsr.rangeEnd() - vsr.rangeStart() + 1);
        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.set(HttpHeaders.CONTENT_RANGE,
            "bytes " + vsr.rangeStart() + "-" + vsr.rangeEnd() + "/" + vsr.fileSize());

        HttpStatus status = vsr.partial() ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(status).headers(headers).body(vsr.resource());
    }
}
