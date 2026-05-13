package uz.asadbek.subcourse.filestorage.dto;

import lombok.Builder;
import org.springframework.core.io.Resource;

@Builder
public record FileResource(String fileKey, String fileName, String contentType, long size,
                           String checksum, Resource resource) {

}
