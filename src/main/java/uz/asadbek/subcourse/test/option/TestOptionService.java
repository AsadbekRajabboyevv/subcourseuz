package uz.asadbek.subcourse.test.option;

import java.util.List;
import uz.asadbek.subcourse.test.option.dto.TestOptionResponseDto;

public interface TestOptionService {

    List<TestOptionResponseDto> getByQuestionIds(List<Long> questionIds);

    List<TestOptionEntity> saveAll(List<TestOptionEntity> savedOptions);

    void deleteById(Long deleteOptionId);

    List<TestOptionEntity> findByQuestionId(Long questionId);

    List<TestOptionEntity> findByQuestionId(List<Long> questionIds);
}
