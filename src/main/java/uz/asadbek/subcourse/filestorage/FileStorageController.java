package uz.asadbek.subcourse.filestorage;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.subcourse.util.ExceptionUtil;

import java.net.URI;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.filestorage.dto.*;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @GetMapping("/{fileKey}")
    public ResponseEntity<Resource> download(@PathVariable String fileKey) {
        FileResource fr = fileStorageService.get(fileKey)
            .orElseThrow(() -> ExceptionUtil.notFoundException("file_not_found"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fr.contentType()));
        headers.setContentLength(fr.size());
        headers.setContentDisposition(
            ContentDisposition.inline().filename(fr.fileName()).build());
        if (fr.checksum() != null) {
            headers.set("X-Content-Checksum", fr.checksum());
        }

        return ResponseEntity.ok().headers(headers).body(fr.resource());
    }

    @GetMapping("/{fileKey}/metadata")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileMetadata> metadata(@PathVariable String fileKey) {
        return ResponseEntity.ok(fileStorageService.getMetadata(fileKey));
    }

    @DeleteMapping("/{fileKey}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> softDelete(
        @PathVariable String fileKey
    ) {
        fileStorageService.softDelete(fileKey);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{fileKey}/hard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> hardDelete(@PathVariable String fileKey) {
        fileStorageService.hardDelete(fileKey);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{fileKey}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> restore(@PathVariable String fileKey) {
        fileStorageService.restore(fileKey);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/videos/init")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VideoUploadSession> initVideoUpload(
        @RequestBody VideoUploadInitRequestDto request
    ) {
        VideoUploadSession session = fileStorageService.initVideoUpload(request);
        return ResponseEntity.ok(session);
    }

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

    @PostMapping("/videos/complete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileUploadResponse> completeVideoUpload(
        @RequestParam String sessionId
    ) {
        FileUploadResponse response = fileStorageService.completeVideoUpload(sessionId);
        return ResponseEntity
            .created(URI.create("/api/v1/files/" + response.getFileKey()))
            .body(response);
    }

    @DeleteMapping("/videos/abort")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> abortVideoUpload(@RequestParam String sessionId) {
        fileStorageService.abortVideoUpload(sessionId);
        return ResponseEntity.noContent().build();
    }

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
            STR."bytes \{vsr.rangeStart()}-\{vsr.rangeEnd()}/\{vsr.fileSize()}");

        HttpStatus status = vsr.partial() ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(status).headers(headers).body(vsr.resource());
    }
}
