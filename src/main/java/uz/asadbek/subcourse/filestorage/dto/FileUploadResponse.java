package uz.asadbek.subcourse.filestorage.dto;

import java.time.Instant;
import lombok.Builder;

@Builder
public record FileUploadResponse(String fileKey, String originalName, String contentType, long size,
                                 String checksum, String url, boolean isPublic,
                                 Instant uploadedAt) {

}
