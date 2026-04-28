package uz.asadbek.subcourse.course.lesson;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonRequestDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonUpdateRequestDto;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CourseLessonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courseId", ignore = true)
    CourseLessonEntity toEntity(CourseLessonRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget CourseLessonEntity entity, CourseLessonUpdateRequestDto dto);
}
