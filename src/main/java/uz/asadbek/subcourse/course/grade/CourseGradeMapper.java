package uz.asadbek.subcourse.course.grade;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeRequestDto;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeUpdateRequestDto;
import uz.asadbek.subcourse.util.MultiLangMapperSupport;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CourseGradeMapper extends MultiLangMapperSupport {

    @Mapping(target = "id", ignore = true)
    CourseGradeEntity toEntity(CourseGradeRequestDto dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget CourseGradeEntity entity, CourseGradeUpdateRequestDto dto);

    @AfterMapping
    default void fill(@MappingTarget CourseGradeEntity entity) {
        fillName(entity.getName());
        fillDescription(entity.getDescription());
    }
}
