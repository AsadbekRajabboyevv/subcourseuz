package uz.asadbek.subcourse.filestorage.dto;

import lombok.Builder;
import org.springframework.core.io.Resource;

@Builder
public record VideoStreamResource(Resource resource, String contentType, long fileSize,
                                  long rangeStart, long rangeEnd, boolean partial) {

}
