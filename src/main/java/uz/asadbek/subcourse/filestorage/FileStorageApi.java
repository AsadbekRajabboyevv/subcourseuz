package uz.asadbek.subcourse.filestorage;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/api/files")
public interface FileStorageApi {

    @GetMapping("/{fileKey}/download")
    ResponseEntity<Resource> getFile(@PathVariable String fileKey);

    @DeleteMapping("/{fileKey}")
    ResponseEntity<Void> deleteFile(@PathVariable String fileKey);
}
