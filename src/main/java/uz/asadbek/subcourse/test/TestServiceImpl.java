package uz.asadbek.subcourse.test;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.dto.TestResultDto;
import uz.asadbek.subcourse.test.dto.TestReviewDto;
import uz.asadbek.subcourse.test.filter.TestFilter;

@Slf4j
@Service
@Transactional
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    public TestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public Long count() {
        return testRepository.countAllByDeletedAtIsNull();
    }

    @Override
    public Long createTest(TestRequestDto testRequestDto) {
        return 0L;
    }

    @Override
    public Page<TestResponseDto> getAllTest(TestFilter filter, Pageable pageable) {
        return null;
    }

    @Override
    public TestResponseDto get(Long id) {
        return null;
    }

    @Override
    public void deleteTestById(Long id) {

    }

    @Override
    public TestResponseDto updateTest(Long id, TestRequestDto testRequestDto) {
        return null;
    }

    @Override
    public Long startTest(Long testId) {
        return 0L;
    }

    @Override
    public void finishTest(Long testId, Long sessionId) {

    }

    @Override
    public void submitAnswer(Long sessionId, Long questionId, Long optionId) {

    }

    @Override
    public TestResultDto getResult(Long sessionId) {
        return null;
    }

    @Override
    public List<TestReviewDto> getReview(Long sessionId) {
        return List.of();
    }

    @Override
    public Long getPrice(Long testId) {
        return 0L;
    }

    @Override
    public void enroll(Long userId, Long testId) {

    }

    @Override
    public void unenroll(Long userId, Long testId) {

    }
}
