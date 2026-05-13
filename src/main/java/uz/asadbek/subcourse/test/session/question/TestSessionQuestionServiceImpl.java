package uz.asadbek.subcourse.test.session.question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.asadbek.subcourse.test.option.TestOptionEntity;
import uz.asadbek.subcourse.test.option.TestOptionService;
import uz.asadbek.subcourse.test.question.TestQuestionEntity;
import uz.asadbek.subcourse.test.session.option.TestSessionOptionEntity;
import uz.asadbek.subcourse.test.session.option.TestSessionOptionService;
import uz.asadbek.subcourse.test.session.option.dto.TestSessionOptionResponseDto;
import uz.asadbek.subcourse.test.session.question.dto.TestSessionQuestionResponseDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestSessionQuestionServiceImpl implements TestSessionQuestionService {

    private final TestSessionQuestionRepository repository;
    private final TestOptionService optionService;
    private final TestSessionOptionService testSessionOptionService;

    @Override
    public void initializeSessionQuestions(Long sessionId, List<TestQuestionEntity> questions) {
        var questionIds = questions.stream().map(TestQuestionEntity::getId).toList();
        var allOptions = optionService.findByQuestionIds(questionIds);
        var optionsMap = allOptions.stream().collect(Collectors.groupingBy(
            TestOptionEntity::getQuestionId
        ));

        List<TestSessionQuestionEntity> sessionQuestions =
            IntStream.range(0, questions.size())
                .mapToObj(i -> {
                    var question = questions.get(i);
                    return new TestSessionQuestionEntity(
                        sessionId,
                        question.getId(),
                        i + 1,
                        question.getText(),
                        question.getImagePath(),
                        question.getCorrectOptionId()
                    );
                })
                .toList();

        repository.saveAll(sessionQuestions);

        List<TestSessionOptionEntity> sessionOptions = new ArrayList<>();
        for (var sessionQuestion : sessionQuestions) {

            var options = optionsMap.getOrDefault(
                sessionQuestion.getQuestionId(),
                Collections.emptyList()
            );

            List<TestOptionEntity> shuffledOptions = new ArrayList<>(options);

            Collections.shuffle(shuffledOptions);

            for (int i = 0; i < shuffledOptions.size(); i++) {
                var option = shuffledOptions.get(i);

                sessionOptions.add(
                    new TestSessionOptionEntity(
                        sessionId,
                        sessionQuestion.getQuestionId(),
                        option.getId(),
                        option.getText(),
                        option.getImagePath(),
                        i + 1
                    )
                );
            }
        }
        testSessionOptionService.saveAll(sessionOptions);
    }
    @Override
    public List<TestSessionQuestionResponseDto> findBySessionId(Long sessionId) {
        List<TestSessionQuestionResponseDto> questions = repository.findBySessionId(sessionId);

        if (questions == null || questions.isEmpty()) {
            return Collections.emptyList();
        }

        List<TestSessionOptionResponseDto> options = testSessionOptionService.findBySessionId(sessionId);

        Map<Long, List<TestSessionOptionResponseDto>> optionsMap = options.stream()
            .filter(o -> o.getQuestionId() != null)
            .collect(Collectors.groupingBy(TestSessionOptionResponseDto::getQuestionId));

        questions.forEach(question -> {
            Long qId = question.getId();

            List<TestSessionOptionResponseDto> questionOptions = optionsMap.get(qId);

            question.setOptions(questionOptions != null ? questionOptions : Collections.emptyList());

            if (questionOptions == null || questionOptions.isEmpty()) {
                log.info("Savol uchun variantlar topilmadi. ID: {}, Map Keys: {}", qId, optionsMap.keySet());
            }
        });

        return questions;
    }
}
