package uz.asadbek.subcourse.test.question;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import uz.asadbek.subcourse.test.question.dto.TestQuestionUpdateRequestDto;

@Mapper(componentModel = ComponentModel.SPRING)
public interface TestQuestionMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget TestQuestionEntity entity, TestQuestionUpdateRequestDto dto);
}
