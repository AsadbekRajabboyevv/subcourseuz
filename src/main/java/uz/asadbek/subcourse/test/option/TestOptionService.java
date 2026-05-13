package uz.asadbek.subcourse.test.option;

import java.util.List;
import uz.asadbek.subcourse.test.option.dto.TestOptionResponseDto;

public interface TestOptionService {

    List<TestOptionResponseDto> getByQuestionIds(List<Long> questionIds);

    void saveAll(List<TestOptionEntity> savedOptions);

    void deleteAllByIds(List<Long> deleteOptionIds);

    List<TestOptionEntity> findByQuestionIds(List<Long> questionIds);
}
