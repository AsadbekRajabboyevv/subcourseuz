package uz.asadbek.subcourse.test.session.option;

import java.util.List;
import uz.asadbek.subcourse.test.session.option.dto.TestSessionOptionResponseDto;

public interface TestSessionOptionService {

    void saveAll(List<TestSessionOptionEntity> savedOptions);

    List<TestSessionOptionResponseDto> findBySessionIdAndQuestionIds(Long sessionId, List<Long> questionIds);
}
