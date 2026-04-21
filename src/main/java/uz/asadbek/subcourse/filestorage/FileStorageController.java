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

package uz.asadbek.subcourse.filestorage;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.filestorage.dto.*;
import uz.asadbek.subcourse.util.ExceptionUtil;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;

    // ─────────────────────────────────────────────────────────────────────────
    //  Regular file upload
    // ─────────────────────────────────────────────────────────────────────────

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileUploadResponse> upload(
        @RequestPart("file")                   MultipartFile file,
        @RequestParam(defaultValue = "")       String  folder,
        @RequestParam(defaultValue = "false")  boolean publicAccess,
        @RequestParam(required = false)        String  ownerId,
        @RequestParam(required = false)        String  allowedMimeTypes
    ) {
        var options = new FileUploadOptions(folder.isBlank() ? null : folder, publicAccess, 0L, allowedMimeTypes, ownerId)
            .folder(folder.isBlank() ? null : folder)
            .publicAccess(publicAccess)
            .ownerId(ownerId)
            .allowedMimeTypes(allowedMimeTypes)
            .build();

        FileUploadResponse response = fileStorageService.upload(file, options);
        return ResponseEntity
            .created(URI.create(STR."/api/v1/files/\{response.fileKey()}"))
            .body(response);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Download (regular files)
    // ─────────────────────────────────────────────────────────────────────────

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

    // ─────────────────────────────────────────────────────────────────────────
    //  Metadata
    // ─────────────────────────────────────────────────────────────────────────

    @GetMapping("/{fileKey}/metadata")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileMetadata> metadata(@PathVariable String fileKey) {
        return ResponseEntity.ok(fileStorageService.getMetadata(fileKey));
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Delete / Restore
    // ─────────────────────────────────────────────────────────────────────────

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

    // ─────────────────────────────────────────────────────────────────────────
    //  Video — chunked upload
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * POST /api/v1/files/videos/init
     * Body: { fileName, contentType, totalSize, totalChunks, folder?, publicAccess?, ownerId? }
     * Returns a sessionId the client uses for subsequent chunk uploads.
     */
    @PostMapping("/videos/init")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VideoUploadSession> initVideoUpload(
        @RequestBody VideoUploadInitRequestDto request
    ) {
        VideoUploadSession session = fileStorageService.initVideoUpload(request);
        return ResponseEntity.ok(session);
    }

    /**
     * POST /api/v1/files/videos/chunk?sessionId=&chunkIndex=
     * Body: multipart chunk
     * Client can send chunks in any order or in parallel.
     */
    @PostMapping(value = "/videos/chunk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> uploadChunk(
        @RequestParam String        sessionId,
        @RequestParam int           chunkIndex,
        @RequestPart("chunk") MultipartFile chunk
    ) {
        fileStorageService.uploadChunk(sessionId, chunkIndex, chunk);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/v1/files/videos/complete?sessionId=
     * Triggers merge → DB record creation → returns final FileUploadResponse.
     */
    @PostMapping("/videos/complete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileUploadResponse> completeVideoUpload(
        @RequestParam String sessionId
    ) {
        FileUploadResponse response = fileStorageService.completeVideoUpload(sessionId);
        return ResponseEntity
            .created(URI.create("/api/v1/files/" + response.fileKey()))
            .body(response);
    }

    /**
     * DELETE /api/v1/files/videos/abort?sessionId=
     * Client or server can call this on error to free temp storage.
     */
    @DeleteMapping("/videos/abort")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> abortVideoUpload(@RequestParam String sessionId) {
        fileStorageService.abortVideoUpload(sessionId);
        return ResponseEntity.noContent().build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Video — HTTP range streaming
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * GET /api/v1/files/videos/{fileKey}
     * Supports Range header → 206 Partial Content for browsers and mobile players.
     * Without Range → 200 full response.
     */
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
