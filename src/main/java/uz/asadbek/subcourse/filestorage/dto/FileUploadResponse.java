package uz.asadbek.subcourse.filestorage.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FileUploadResponse {

   private String fileKey;
   private String originalName;
   private String contentType;
   private long size;
   private String checksum;
   private String url;
   private boolean isPublic;
   private LocalDateTime uploadedAt;

}
