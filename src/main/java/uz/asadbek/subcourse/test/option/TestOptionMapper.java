package uz.asadbek.subcourse.test.option;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import uz.asadbek.subcourse.test.option.dto.TestOptionResponseDto;
import uz.asadbek.subcourse.test.option.dto.TestOptionUpdateDto;

@Mapper(componentModel = ComponentModel.SPRING)
public interface TestOptionMapper {

    @Mapping(target = "imageUrl", source = "imagePath")
    TestOptionResponseDto toResponse(TestOptionEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget TestOptionEntity entity, TestOptionUpdateDto dto);
}
