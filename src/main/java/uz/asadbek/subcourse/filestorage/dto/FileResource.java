package uz.asadbek.subcourse.filestorage.dto;

import java.io.InputStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResource {

    private String fileKey;
    private String fileName;
    private InputStream inputStream;
    private String contentType;
    private Long size;
}
