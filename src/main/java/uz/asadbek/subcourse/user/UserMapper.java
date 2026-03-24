package uz.asadbek.subcourse.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import uz.asadbek.base.mapper.BaseMapper;
import uz.asadbek.subcourse.user.dto.UserRequestDto;
import uz.asadbek.subcourse.user.dto.UserResponseDto;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "confirmationToken", ignore = true)
    UserEntity toEntity(UserRequestDto dto);

    @Mapping(target = "position", ignore = true)
    UserResponseDto toDto(UserEntity entity);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    void partialUpdate(@MappingTarget UserEntity entity, UserRequestDto dto);
}
