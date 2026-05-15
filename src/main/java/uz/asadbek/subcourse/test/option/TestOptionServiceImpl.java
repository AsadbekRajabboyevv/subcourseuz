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
    public List<TestOptionEntity> saveAll(List<TestOptionEntity> savedOptions) {
        return repository.saveAll(savedOptions);
    }

    @Override
    @Transactional
    public void deleteById(Long deleteOptionId) {
        repository.deleteById(deleteOptionId);
    }

    @Override
    public List<TestOptionEntity> findByQuestionId(Long questionId) {
        return repository.findByQuestionId(questionId);
    }

    @Override
    public List<TestOptionEntity> findByQuestionId(List<Long> questionIds) {
        return repository.findByQuestionIds(questionIds);
    }
}
