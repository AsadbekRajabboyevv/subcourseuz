package uz.asadbek.subcourse.filestorage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse {

    private String fileKey;     // unique identifier (UUID)
    private String fileName;    // stored name
    private Long size;          // bytes
    private String contentType; // MIME
    private String url;         // public URL
}
