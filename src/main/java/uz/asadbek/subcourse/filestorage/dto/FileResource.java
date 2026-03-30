package uz.asadbek.subcourse.filestorage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResource {

    private String fileKey;
    private String fileName;
    private Resource resource;
    private String contentType;
    private Long size;
}
