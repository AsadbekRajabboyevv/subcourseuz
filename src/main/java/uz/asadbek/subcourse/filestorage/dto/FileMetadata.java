package uz.asadbek.subcourse.filestorage.dto;

import java.time.Instant;
import lombok.Builder;

@Builder
public record FileMetadata(String fileKey, String originalName, String contentType, long size,
                           String checksum, boolean isPublic, String folder, long downloadCount,
                           Instant uploadedAt, Instant lastAccessedAt) {

}
