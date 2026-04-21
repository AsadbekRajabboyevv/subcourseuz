package uz.asadbek.subcourse.filestorage;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import uz.asadbek.subcourse.filestorage.dto.FileMetadata;
import uz.asadbek.subcourse.filestorage.dto.FileUploadResponse;


@Mapper(componentModel = ComponentModel.SPRING)
public interface FileStorageMapper {

    @Mapping(target = "uploadedAt", source = "createdAt")
    @Mapping(target = "isPublic", source = "isPublic")
    FileUploadResponse toUploadResponse(FileStorageEntity entity);

    @Mapping(target = "uploadedAt", source = "createdAt")
    @Mapping(target = "isPublic", source = "isPublic")
    FileMetadata toMetadata(FileStorageEntity entity);
}

