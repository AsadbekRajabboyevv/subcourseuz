package uz.asadbek.subcourse.comment;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import uz.asadbek.subcourse.comment.dto.CommentRequestDto;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    CommentEntity toEntity(CommentRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void update(@MappingTarget CommentEntity entity, CommentRequestDto dto);
}
