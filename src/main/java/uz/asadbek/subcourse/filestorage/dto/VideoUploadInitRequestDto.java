package uz.asadbek.subcourse.filestorage.dto;

import lombok.Builder;

@Builder
public record VideoUploadInitRequestDto(String fileName, String contentType, long totalSize,
                                        int totalChunks, String folder, boolean publicAccess,
                                        Long ownerId) {

}
