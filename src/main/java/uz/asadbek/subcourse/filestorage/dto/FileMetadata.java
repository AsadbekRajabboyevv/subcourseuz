package uz.asadbek.subcourse.filestorage.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record FileMetadata(String fileKey, String originalName, String contentType, long size,
                           String checksum, boolean isPublic, String folder, long downloadCount,
                           LocalDateTime uploadedAt, LocalDateTime lastAccessedAt) {

}
