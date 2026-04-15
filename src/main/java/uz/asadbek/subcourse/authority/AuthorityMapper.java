package uz.asadbek.subcourse.authority;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import uz.asadbek.subcourse.authority.dto.AuthorityRequestDto;
import uz.asadbek.subcourse.authority.dto.AuthorityResponseDto;

@Mapper(componentModel = ComponentModel.SPRING)
public interface AuthorityMapper {

    AuthorityResponseDto toResponseDto(AuthorityEntity entity);

    @Mapping(target = "id", ignore = true)
    AuthorityEntity toEntity(AuthorityRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget AuthorityEntity entity, AuthorityRequestDto requestDto);
}
