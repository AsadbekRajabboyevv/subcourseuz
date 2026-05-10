package uz.asadbek.subcourse.test.session.option;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.test.session.option.dto.TestSessionOptionResponseDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestSessionOptionServiceImpl implements TestSessionOptionService {

    private final TestSessionOptionRepository repository;

    @Override
    @Transactional
    public void saveAll(List<TestSessionOptionEntity> savedOptions) {
        repository.saveAll(savedOptions);
    }

    @Override
    public List<TestSessionOptionResponseDto> findBySessionIdAndQuestionIds(Long sessionId,
        List<Long> questionIds) {
        return repository.findBySessionIdAndSessionQuestionIds(sessionId, questionIds);
    }
}
