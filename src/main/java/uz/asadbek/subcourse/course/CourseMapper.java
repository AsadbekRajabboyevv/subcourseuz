package uz.asadbek.subcourse.course;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import uz.asadbek.subcourse.course.dto.CourseRequestDto;
import uz.asadbek.subcourse.course.dto.CourseUpdateRequestDto;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isVideoCourse", expression = "java(Boolean.TRUE.equals(dto.getIsVideoCourse()))")
    @Mapping(target = "isPublished", expression = "java(Boolean.TRUE.equals(dto.getIsPublished()))")
    @Mapping(target = "lang", source = "lang")
    @Mapping(target = "ownerId", expression = "java(uz.asadbek.subcourse.util.JwtUtil.getCurrentUser().getId())")
    CourseEntity toEntity(CourseRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget CourseEntity entity, CourseUpdateRequestDto dto);
}
