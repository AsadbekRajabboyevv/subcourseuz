package uz.asadbek.subcourse.test.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.ai.dto.GeminiTestGenerateResponseDto;
import uz.asadbek.subcourse.exception.NotFoundException;
import uz.asadbek.subcourse.filestorage.FileStorageService;
import uz.asadbek.subcourse.filestorage.dto.FileUploadOptions;
import uz.asadbek.subcourse.test.option.TestOptionService;
import uz.asadbek.subcourse.test.question.TestQuestionService;
import uz.asadbek.subcourse.test.test.dto.TestRequestDto;
import uz.asadbek.subcourse.test.test.dto.TestResponseDto;
import uz.asadbek.subcourse.test.test.dto.TestUpdateRequestDto;
import uz.asadbek.subcourse.test.test.filter.TestFilter;
import uz.asadbek.subcourse.test.option.TestOptionEntity;
import uz.asadbek.subcourse.test.option.TestOptionMapper;
import uz.asadbek.subcourse.test.option.dto.TestOptionResponseDto;
import uz.asadbek.subcourse.test.question.TestQuestionEntity;
import uz.asadbek.subcourse.test.question.TestQuestionMapper;
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
    private final TestQuestionService questionService;
    private final TestOptionService optionService;
    private final FileStorageService fileStorageService;
    private final TestQuestionMapper questionMapper;
    private final TestOptionMapper optionMapper;
    private final Validator validator;
    private final TestMapper mapper;

    @Override
    public Long count() {
        return repository.countAllByDeletedAtIsNullAndIsPublishedIsTrue();
    }

    @Override
    public Page<TestResponseDto> get(TestFilter filter, Pageable pageable) {
        return repository.get(filter, pageable);
    }

    @Override
    public TestResponseDto getInfo(Long id) {
        return repository.get(id).orElseThrow(
            () -> ExceptionUtil.build(NotFoundException.class, "error.not_found.test", id));
    }

    @Override
    public TestResponseDto get(Long id) {
        var test = repository.get(id).orElseThrow(
            () -> ExceptionUtil.build(NotFoundException.class, "error.not_found.test", id));
        var questions = questionService.getByTestId(id);
        if (questions.isEmpty()) {
            test.setQuestions(List.of());
            return test;
        }
        var questionIds = questions.stream().map(TestQuestionResponseDto::getId).toList();
        var options = optionService.getByQuestionIds(questionIds);
        var optionMap = options.stream()
            .collect(Collectors.groupingBy(TestOptionResponseDto::getQuestionId));
        questions.forEach(
            question -> question.setOptions(optionMap.getOrDefault(question.getId(), List.of())));
        test.setQuestions(questions);

        return test;
    }

    @Override
    @Transactional
    public Long create(TestRequestDto request, MultipartFile image, MultipartFile[] questionImages,
        MultipartFile[] optionImages) {
        validator.validateTest(request);

        String testImage = null;
        if (image != null && !image.isEmpty()) {
            testImage = fileStorageService.upload(image, FileUploadOptions.TEST_IMAGE).getUrl();
        }

        var test = mapper.toEntity(request);
        test.setImagePath(testImage);
        repository.save(test);

        var qImgIdx = 0;
        var oImgIdx = 0;

        for (int i = 0; i < request.getQuestions().size(); i++) {
            TestQuestionRequestDto questionDto = request.getQuestions().get(i);

            String questionImage = null;
            if (questionImages != null && qImgIdx < questionImages.length) {
                MultipartFile qFile = questionImages[qImgIdx++];
                if (!qFile.isEmpty()) {
                    questionImage = fileStorageService.upload(qFile,
                        FileUploadOptions.QUESTION_IMAGE).getUrl();
                }
            }

            var question = new TestQuestionEntity();
            question.setText(questionDto.getText());
            question.setImagePath(questionImage);
            question.setTestId(test.getId());
            questionService.save(question);

            List<TestOptionEntity> savedOptions = new ArrayList<>();

            for (int j = 0; j < questionDto.getOptions().size(); j++) {
                var optionDto = questionDto.getOptions().get(j);

                String optionImage = null;
                if (optionImages != null && oImgIdx < optionImages.length) {
                    var oFile = optionImages[oImgIdx++];
                    if (!oFile.isEmpty()) {
                        optionImage = fileStorageService.upload(oFile,
                            FileUploadOptions.OPTION_IMAGE).getUrl();
                    }
                }

                var option = new TestOptionEntity();
                option.setText(optionDto.getText());
                option.setImagePath(optionImage);
                option.setQuestionId(question.getId());
                savedOptions.add(option);
            }

            optionService.saveAll(savedOptions);

            if (questionDto.getCorrectOptionIndex() != null) {
                var correctOption = savedOptions.get(questionDto.getCorrectOptionIndex());
                question.setCorrectOptionId(correctOption.getId());
                questionService.save(question);
            }
        }

        return test.getId();
    }

    @Override
    @Transactional
    public Long update(Long id, TestUpdateRequestDto request, MultipartFile image,
        MultipartFile[] qFiles, MultipartFile[] oFiles) {

        var test = repository.findById(id).orElseThrow(
            () -> ExceptionUtil.build(NotFoundException.class, "error.not_found.test", id));
        Map<String, MultipartFile> qFileMap = new HashMap<>();

        if (qFiles != null) {
            for (MultipartFile file : qFiles) {
                if (file.getOriginalFilename() != null) {
                    qFileMap.put(file.getOriginalFilename(), file);
                }
            }
        }

        Map<String, MultipartFile> oFileMap = new HashMap<>();

        if (oFiles != null) {
            for (MultipartFile file : oFiles) {
                if (file.getOriginalFilename() != null) {
                    oFileMap.put(file.getOriginalFilename(), file);
                }
            }
        }

        mapper.update(test, request);

        if (image != null && !image.isEmpty()) {
            test.setImagePath(
                fileStorageService.upload(image, FileUploadOptions.TEST_IMAGE).getUrl());
        }

        validator.validateTestForUpdate(test);

        if (request.getQuestions() != null) {
            var existingQuestions = questionService.findByTestId(test.getId());
            Map<Long, TestQuestionEntity> questionMap = existingQuestions.stream()
                .collect(Collectors.toMap(TestQuestionEntity::getId, q -> q));
            var questionIds = existingQuestions.stream().map(TestQuestionEntity::getId).toList();
            var allOptions = optionService.findByQuestionIds(questionIds);

            Map<Long, List<TestOptionEntity>> optionsMap = allOptions.stream()
                .collect(Collectors.groupingBy(TestOptionEntity::getQuestionId));

            Set<Long> requestQuestionIds = new HashSet<>();

            for (int i = 0; i < request.getQuestions().size(); i++) {
                var qDto = request.getQuestions().get(i);

                TestQuestionEntity question;
                if (qDto.getId() == null) {
                    question = new TestQuestionEntity();
                    question.setTestId(test.getId());

                } else {
                    question = questionMap.get(qDto.getId());
                    if (question == null) {
                        throw ExceptionUtil.build(NotFoundException.class,
                            "error.not_found.question");
                    }

                    requestQuestionIds.add(question.getId());
                }

                questionMapper.update(question, qDto);
                String qPrefix = "q_" + i + ".";
                String finalQKey = findFileByKey(qFileMap, qPrefix);

                if (finalQKey != null) {
                    question.setImagePath(fileStorageService.upload(qFileMap.get(finalQKey),
                        FileUploadOptions.QUESTION_IMAGE).getUrl());
                }

                questionService.save(question);

                if (qDto.getOptions() != null) {

                    var existingOptions = optionsMap.getOrDefault(question.getId(),
                        Collections.emptyList());

                    Map<Long, TestOptionEntity> optionMap = existingOptions.stream()
                        .collect(Collectors.toMap(TestOptionEntity::getId, o -> o));
                    Set<Long> requestOptionIds = new HashSet<>();
                    List<TestOptionEntity> savedOptions = new ArrayList<>();
                    for (int j = 0; j < qDto.getOptions().size(); j++) {
                        var oDto = qDto.getOptions().get(j);
                        TestOptionEntity option;
                        if (oDto.getId() == null) {

                            option = new TestOptionEntity();
                            option.setQuestionId(question.getId());

                        } else {
                            option = optionMap.get(oDto.getId());
                            if (option == null) {
                                throw ExceptionUtil.build(NotFoundException.class,
                                    "error.not_found.option");
                            }

                            requestOptionIds.add(option.getId());
                        }

                        optionMapper.update(option, oDto);

                        String oPrefix = "q_" + i + "_opt_" + j + ".";

                        String finalOKey = findFileByKey(oFileMap, oPrefix);

                        if (finalOKey != null) {
                            option.setImagePath(fileStorageService.upload(oFileMap.get(finalOKey),
                                FileUploadOptions.OPTION_IMAGE).getUrl());
                        }

                        savedOptions.add(option);
                    }

                    optionService.saveAll(savedOptions);

                    List<Long> deleteOptionIds = existingOptions.stream()
                        .map(TestOptionEntity::getId)
                        .filter(optionId -> !requestOptionIds.contains(optionId)).toList();

                    if (!deleteOptionIds.isEmpty()) {
                        optionService.deleteAllByIds(deleteOptionIds);
                    }

                    if (qDto.getCorrectOptionIndex() != null) {
                        int idx = qDto.getCorrectOptionIndex();
                        if (idx < savedOptions.size()) {
                            question.setCorrectOptionId(savedOptions.get(idx).getId());
                            questionService.save(question);
                        }
                    }
                }
            }

            List<Long> deleteQuestionIds = existingQuestions.stream().map(TestQuestionEntity::getId)
                .filter(questionId -> !requestQuestionIds.contains(questionId)).toList();

            if (!deleteQuestionIds.isEmpty()) {
                questionService.deleteAllByIds(deleteQuestionIds);
            }
        }

        return test.getId();
    }

    private String findFileByKey(Map<String, MultipartFile> fileMap, String prefix) {
        return fileMap.keySet().stream().filter(key -> key.startsWith(prefix)).findFirst()
            .orElse(null);
    }

    @Override
    @Transactional
    public void enroll(Long testId) {
        var userId = JwtUtil.getCurrentUserId();
        validator.validateEnroll(userId, testId, repository::existsById, "error.not_found.test");
        var uc = new UserTestEntity();
        uc.setId(new UserPurchaseId(userId, testId, LocalDateTime.now()));
    }

    @Override
    @Transactional
    public void saveAiGeneratedTest(GeminiTestGenerateResponseDto request, Long testId) {
        List<TestQuestionEntity> questions = request.getQuestions().stream()
            .map(dto -> {
                var q = new TestQuestionEntity();
                q.setTestId(testId);
                q.setText(dto.getText());
                return q;
            }).collect(Collectors.toList());

        var savedQuestions = questionService.saveAll(questions);

        List<TestOptionEntity> allOptionsToSave = new ArrayList<>();
        Map<Integer, List<TestOptionEntity>> questionIndexToOptionsMap = new HashMap<>();

        for (int i = 0; i < savedQuestions.size(); i++) {
            var questionEntity = savedQuestions.get(i);
            var questionDto = request.getQuestions().get(i);
            List<TestOptionEntity> currentQuestionOptions = new ArrayList<>();

            for (var optDto : questionDto.getOptions()) {
                var option = new TestOptionEntity();
                option.setQuestionId(questionEntity.getId());
                option.setText(optDto.getText());

                currentQuestionOptions.add(option);
                allOptionsToSave.add(option);
            }
            questionIndexToOptionsMap.put(i, currentQuestionOptions);
        }

        optionService.saveAll(allOptionsToSave);

        for (int i = 0; i < savedQuestions.size(); i++) {
            var questionEntity = savedQuestions.get(i);
            var questionDto = request.getQuestions().get(i);
            var optionsOfThisQuestion = questionIndexToOptionsMap.get(i);

            var correctIdx = questionDto.getCorrectOptionIndex();
            if (correctIdx != null && correctIdx >= 0 && correctIdx < optionsOfThisQuestion.size()) {
                var correctOptionEntity = optionsOfThisQuestion.get(correctIdx);
                questionEntity.setCorrectOptionId(correctOptionEntity.getId());
            }
        }

        questionService.saveAll(savedQuestions);
    }

    @Override
    @Transactional
    public void save(TestEntity newTest) {
        repository.save(newTest);
    }

    @Override
    @Transactional
    public Long publish(Long id) {
        int updated = repository.publish(id);
        if (updated == 0) {
            throw ExceptionUtil.build(NotFoundException.class, "error.not_found.test");
        }
        return id;
    }

    @Override
    @Transactional
    public Long unpublish(Long id) {
        int updated = repository.unpublish(id);
        if (updated == 0) {
            throw ExceptionUtil.build(NotFoundException.class, "error.not_found.test");
        }

        return id;
    }

    @Override
    public TestEntity getById(Long id) {
        return repository.findById(id).orElseThrow(
            () -> ExceptionUtil.build(NotFoundException.class, "error.not_found.test", id));
    }

}
