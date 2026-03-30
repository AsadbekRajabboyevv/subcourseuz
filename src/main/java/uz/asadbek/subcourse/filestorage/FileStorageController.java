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

@RestController
@RequiredArgsConstructor
public class FileStorageController implements FileStorageApi {

    private final FileStorageService fileStorageService;

    @GetMapping("/{fileKey}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileKey) {
        var file = fileStorageService.get(fileKey)
            .orElseThrow(() -> ExceptionUtil.notFoundException("file_not_found"));

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(file.getContentType()))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                STR."inline; filename=\"\{file.getFileName()}\"")
            .body(file.getResource());
    }

    @Override
    public ResponseEntity<Void> deleteFile(String fileKey) {
        fileStorageService.delete(fileKey);
        return ResponseEntity.noContent().build();
    }
}
