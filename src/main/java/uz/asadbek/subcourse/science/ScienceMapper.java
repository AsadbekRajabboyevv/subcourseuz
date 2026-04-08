package uz.asadbek.subcourse.science;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import uz.asadbek.subcourse.science.dto.ScienceRequestDto;
import uz.asadbek.subcourse.science.dto.ScienceUpdateRequestDto;
import uz.asadbek.subcourse.util.MultiLangMapperSupport;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ScienceMapper extends MultiLangMapperSupport {

    @Mapping(target = "id", ignore = true)
    ScienceEntity toEntity(ScienceRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget ScienceEntity entity, ScienceUpdateRequestDto dto);

    @AfterMapping
    default void fillCrl(@MappingTarget ScienceEntity entity) {
        fillName(entity.getName());
        fillDescription(entity.getDescription());
    }
}
