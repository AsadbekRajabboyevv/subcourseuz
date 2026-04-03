package uz.asadbek.subcourse.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.filestorage.FileStorageService;
import uz.asadbek.subcourse.filestorage.dto.FileUploadOptions;
import uz.asadbek.subcourse.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.dto.TestUpdateRequestDto;
import uz.asadbek.subcourse.test.filter.TestFilter;
import uz.asadbek.subcourse.test.option.TestOptionEntity;
import uz.asadbek.subcourse.test.option.TestOptionMapper;
import uz.asadbek.subcourse.test.option.TestOptionRepository;
import uz.asadbek.subcourse.test.option.dto.TestOptionRequestDto;
import uz.asadbek.subcourse.test.option.dto.TestOptionResponseDto;
import uz.asadbek.subcourse.test.question.TestQuestionEntity;
import uz.asadbek.subcourse.test.question.TestQuestionMapper;
import uz.asadbek.subcourse.test.question.TestQuestionRepository;
import uz.asadbek.subcourse.test.question.dto.TestQuestionRequestDto;
import uz.asadbek.subcourse.test.question.dto.TestQuestionResponseDto;
import uz.asadbek.subcourse.test.usertest.UserTestEntity;
import uz.asadbek.subcourse.util.JwtUtil;
import uz.asadbek.subcourse.util.Validator;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.embedded.UserPurchaseId;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestServiceImpl implements TestService {

    private final TestRepository repository;
    private final Validator validator;
    private final FileStorageService fileStorageService;
    private final TestOptionRepository testOptionRepository;
    private final TestQuestionRepository testQuestionRepository;
    private final TestMapper testMapper;
    private final TestQuestionMapper testQuestionMapper;
    private final TestOptionMapper testOptionMapper;

    @Override
    public Long count() {
        return repository.countAllByDeletedAtIsNullAndIsPublishedIsTrue();
    }

    @Override
    public Page<TestResponseDto> get(TestFilter filter, Pageable pageable) {
        return repository.get(filter, pageable);
    }

    @Override
    public TestResponseDto get(Long id) {
        var test = repository.get(id);
        var questions = testQuestionRepository.getByTestId(id);
        if (questions.isEmpty()) {
            test.setQuestions(List.of());
            return test;
        }
        var questionIds = questions.stream().map(TestQuestionResponseDto::getId).toList();
        var options = testOptionRepository.getByQuestionIds(questionIds);
        var optionMap = options.stream().collect(Collectors.groupingBy(TestOptionResponseDto::getQuestionId));
        questions.forEach(question ->
            question.setOptions(optionMap.getOrDefault(question.getId(), List.of()))
        );
        test.setQuestions(questions);

        return test;
    }
    @Override
    @Transactional
    public Long createTest(TestRequestDto request) {

        validator.validateTest(request);

        String testImage = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            testImage = fileStorageService
                .upload(request.getImage(), new FileUploadOptions().setTestImages())
                .getFileKey();
        }

        var test = testMapper.toEntity(request);
        test.setImagePath(testImage);
        repository.save(test);

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

        validator.validateTestForUpdate(test);

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
    @Transactional
    public void enroll(Long testId) {
        var userId =  JwtUtil.getCurrentUser().getId();
        validator.validateEnroll(userId, testId, repository::existsById, "test_not_found");
        var uc = new UserTestEntity();
        uc.setId(new UserPurchaseId(userId, testId, LocalDateTime.now()));
    }

    @Override
    @Transactional
    public Long publish(Long id) {
        int updated = repository.publish(id);
        if (updated == 0) {
            throw ExceptionUtil.notFoundException("test_not_found");
        }
        return id;
    }

    @Override
    @Transactional
    public Long unpublishTest(Long id) {
        int updated = repository.unpublish(id);
        if (updated == 0) {
            throw ExceptionUtil.notFoundException("test_not_found");
        }

        return id;
    }

}
