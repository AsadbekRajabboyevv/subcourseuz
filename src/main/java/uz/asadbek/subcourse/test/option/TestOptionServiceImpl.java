package uz.asadbek.subcourse.test.option;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.test.option.dto.TestOptionResponseDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestOptionServiceImpl implements TestOptionService {

    private final TestOptionRepository repository;

    @Override
    public List<TestOptionResponseDto> getByQuestionIds(List<Long> questionIds) {
        return repository.getByQuestionIds(questionIds);
    }

    @Override
    @Transactional
    public void saveAll(List<TestOptionEntity> savedOptions) {
        repository.saveAll(savedOptions);
    }

    @Override
    @Transactional
    public void deleteAllByIds(List<Long> deleteOptionIds) {
        repository.deleteAllById(deleteOptionIds);
    }

    @Override
    public List<TestOptionEntity> findByQuestionIds(List<Long> questionIds) {
        return repository.findByQuestionIds(questionIds);
    }
}
