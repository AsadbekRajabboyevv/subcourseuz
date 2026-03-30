package uz.asadbek.subcourse.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.filestorage.FileStorageService;
import uz.asadbek.subcourse.filestorage.dto.FileUploadOptions;
import uz.asadbek.subcourse.test.dto.SubmitAnswerRequestDto;
import uz.asadbek.subcourse.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.dto.TestResultDto;
import uz.asadbek.subcourse.test.dto.TestReviewDto;
import uz.asadbek.subcourse.test.dto.TestUpdateRequestDto;
import uz.asadbek.subcourse.test.filter.TestFilter;
import uz.asadbek.subcourse.test.option.TestOptionEntity;
import uz.asadbek.subcourse.test.option.TestOptionMapper;
import uz.asadbek.subcourse.test.option.TestOptionRepository;
import uz.asadbek.subcourse.test.option.dto.TestOptionRequestDto;
import uz.asadbek.subcourse.test.question.TestQuestionEntity;
import uz.asadbek.subcourse.test.question.TestQuestionMapper;
import uz.asadbek.subcourse.test.question.TestQuestionRepository;
import uz.asadbek.subcourse.test.question.dto.TestQuestionRequestDto;
import uz.asadbek.subcourse.test.validator.TestValidator;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Slf4j
@Service
@Transactional
public class TestServiceImpl implements TestService {

    private final TestRepository repository;
    private final TestValidator testValidator;
    private final FileStorageService fileStorageService;
    private final TestOptionRepository testOptionRepository;
    private final TestQuestionRepository testQuestionRepository;
    private final TestMapper testMapper;
    private final TestQuestionMapper testQuestionMapper;
    private final TestOptionMapper testOptionMapper;

    public TestServiceImpl(TestRepository repository, TestValidator testValidator,
        FileStorageService fileStorageService, TestOptionRepository testOptionRepository,
        TestQuestionRepository testQuestionRepository, TestMapper testMapper,
        TestQuestionMapper testQuestionMapper, TestOptionMapper testOptionMapper) {
        this.repository = repository;
        this.testValidator = testValidator;
        this.fileStorageService = fileStorageService;
        this.testOptionRepository = testOptionRepository;
        this.testQuestionRepository = testQuestionRepository;
        this.testMapper = testMapper;
        this.testQuestionMapper = testQuestionMapper;
        this.testOptionMapper = testOptionMapper;
    }

    @Override
    public Long count() {
        return repository.countAllByDeletedAtIsNullAndIsPublishedIsTrue();
    }

    @Override
    @Transactional
    public Long createTest(TestRequestDto request) {

        testValidator.validate(request);

        // 1. Test image
        String testImage = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            testImage = fileStorageService
                .upload(request.getImage(), new FileUploadOptions().setTestImages())
                .getFileKey();
        }

        var test = testMapper.toEntity(request);
        test.setImagePath(testImage);
        repository.save(test);

        // 3. Questions
        for (TestQuestionRequestDto questionDto : request.getQuestions()) {

            String questionImage = null;
            if (questionDto.getImage() != null && !questionDto.getImage().isEmpty()) {
                questionImage = fileStorageService
                    .upload(questionDto.getImage(), new FileUploadOptions().setTestQuestions())
                    .getFileKey();
            }

            TestQuestionEntity question = new TestQuestionEntity();
            question.setText(questionDto.getText());
            question.setImagePath(questionImage);
            question.setTestId(test.getId());

            testQuestionRepository.save(question);

            List<TestOptionEntity> savedOptions = new ArrayList<>();

            for (TestOptionRequestDto optionDto : questionDto.getOptions()) {

                String optionImage = null;
                if (optionDto.getImage() != null && !optionDto.getImage().isEmpty()) {
                    optionImage = fileStorageService
                        .upload(optionDto.getImage(),
                            new FileUploadOptions().setTestOptionsImages())
                        .getFileKey();
                }

                TestOptionEntity option = new TestOptionEntity();
                option.setText(optionDto.getText());
                option.setImagePath(optionImage);
                option.setQuestionId(question.getId());

                savedOptions.add(option);
            }

            testOptionRepository.saveAll(savedOptions);

            TestOptionEntity correctOption =
                savedOptions.get(questionDto.getCorrectOptionIndex());

            question.setCorrectOptionId(correctOption.getId());
            testQuestionRepository.save(question);
        }

        return test.getId();
    }

    @Override
    public Page<TestResponseDto> getAllTest(TestFilter filter, Pageable pageable) {
        return repository.get(filter, pageable);
    }

    @Override
    public TestResponseDto get(Long id) {
        return repository.get(id);
    }

    @Override
    public Long unpublishTest(Long id) {
        int updated = repository.unpublishTest(id);
        if (updated == 0) {
            throw ExceptionUtil.notFoundException("test_not_found");
        }

        return id;
    }

    @Override
    @Transactional
    public Long updateTest(Long id, TestUpdateRequestDto request) {

        var test = repository.findById(id)
            .orElseThrow(() -> ExceptionUtil.notFoundException("test_not_found"));

        testMapper.update(test, request);

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            var uploaded = fileStorageService.upload(
                request.getImage(),
                new FileUploadOptions().setTestImages()
            );
            test.setImagePath(uploaded.getFileKey());
        }

        testValidator.validateForUpdate(test);

        if (request.getQuestions() != null) {
            var existingQuestions = testQuestionRepository.findByTestId(test.getId());
            Map<Long, TestQuestionEntity> questionMap = existingQuestions.stream()
                .collect(Collectors.toMap(TestQuestionEntity::getId, q -> q));
            Set<Long> requestQuestionIds = new HashSet<>();
            for (var qDto : request.getQuestions()) {
                TestQuestionEntity question;
                if (qDto.getId() == null) {
                    question = new TestQuestionEntity();
                    question.setTestId(test.getId());
                } else {
                    question = questionMap.get(qDto.getId());
                    if (question == null) {
                        throw ExceptionUtil.badRequestException("question_not_found");
                    }
                    requestQuestionIds.add(question.getId());
                }

                testQuestionMapper.update(question, qDto);

                if (qDto.getImage() != null && !qDto.getImage().isEmpty()) {
                    var uploaded = fileStorageService.upload(
                        qDto.getImage(),
                        new FileUploadOptions().setTestQuestions()
                    );
                    question.setImagePath(uploaded.getFileKey());
                }

                testQuestionRepository.save(question);

                if (qDto.getOptions() != null) {

                    var existingOptions = testOptionRepository.findByQuestionId(question.getId());
                    Map<Long, TestOptionEntity> optionMap = existingOptions.stream()
                        .collect(Collectors.toMap(TestOptionEntity::getId, o -> o));

                    Set<Long> requestOptionIds = new HashSet<>();
                    List<TestOptionEntity> savedOptions = new ArrayList<>();

                    for (var oDto : qDto.getOptions()) {

                        TestOptionEntity option;

                        if (oDto.getId() == null) {
                            option = new TestOptionEntity();
                            option.setQuestionId(question.getId());
                        } else {
                            option = optionMap.get(oDto.getId());
                            if (option == null) {
                                throw ExceptionUtil.badRequestException("option_not_found");
                            }
                            requestOptionIds.add(option.getId());
                        }

                        testOptionMapper.update(option, oDto);

                        if (oDto.getImage() != null && !oDto.getImage().isEmpty()) {
                            var uploaded = fileStorageService.upload(
                                oDto.getImage(),
                                new FileUploadOptions().setTestOptionsImages()
                            );
                            option.setImagePath(uploaded.getFileKey());
                        }

                        savedOptions.add(option);
                    }

                    testOptionRepository.saveAll(savedOptions);

                    for (var existing : existingOptions) {
                        if (!requestOptionIds.contains(existing.getId())) {
                            testOptionRepository.delete(existing);
                        }
                    }

                    if (qDto.getCorrectOptionIndex() != null) {
                        int idx = qDto.getCorrectOptionIndex();
                        if (idx >= savedOptions.size()) {
                            throw ExceptionUtil.badRequestException("invalid_correct_option_index");
                        }
                        question.setCorrectOptionId(savedOptions.get(idx).getId());
                        testQuestionRepository.save(question);
                    }
                }
            }

            for (var existing : existingQuestions) {
                if (!requestQuestionIds.contains(existing.getId())) {
                    testQuestionRepository.delete(existing);
                }
            }
        }

        return test.getId();
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

    private TestEntity findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> ExceptionUtil.notFoundException("test_not_found"));
    }
}
