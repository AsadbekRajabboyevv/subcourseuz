package uz.asadbek.subcourse.test;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import uz.asadbek.subcourse.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.dto.TestUpdateRequestDto;

@Mapper(componentModel = ComponentModel.SPRING)
public interface TestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagePath", ignore = true)
    @Mapping(target = "price", expression = "java(dto.getPrice() == null ? 0L : dto.getPrice())")
    @Mapping(target = "isPublished", expression = "java(Boolean.TRUE.equals(dto.getIsPublished()))")
    TestEntity toEntity(TestRequestDto dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget TestEntity entity, TestUpdateRequestDto dto);

}
