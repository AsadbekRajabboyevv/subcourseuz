package uz.asadbek.subcourse.filestorage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileUploadOptions {
    private String folder;          // "avatars/", "courses/"
    private boolean publicAccess;   // CDN / public URL
    private Long maxSize;           // override limit
}
