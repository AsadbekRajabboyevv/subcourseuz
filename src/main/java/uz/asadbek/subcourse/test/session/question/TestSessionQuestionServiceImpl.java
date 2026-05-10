package uz.asadbek.subcourse.test.session.question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.asadbek.subcourse.test.option.TestOptionEntity;
import uz.asadbek.subcourse.test.option.TestOptionService;
import uz.asadbek.subcourse.test.question.TestQuestionEntity;
import uz.asadbek.subcourse.test.session.option.TestSessionOptionEntity;
import uz.asadbek.subcourse.test.session.option.TestSessionOptionService;
import uz.asadbek.subcourse.test.session.option.dto.TestSessionOptionResponseDto;
import uz.asadbek.subcourse.test.session.question.dto.TestSessionQuestionResponseDto;

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
                        i+1,
                        question.getText(),
                        question.getImagePath()
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

            for (int i = 0; i < options.size(); i++) {

                var option = options.get(i);

                sessionOptions.add(
                    new TestSessionOptionEntity(
                        sessionId,
                        sessionQuestion.getId(),
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
        var questions = repository.findBySessionId(sessionId);
        var questionIds = questions.stream().map(TestSessionQuestionResponseDto::getId).toList();
        var options = testSessionOptionService.findBySessionIdAndQuestionIds(
            sessionId, questionIds);
        var optionsMap = options.stream().collect(Collectors
            .groupingBy(TestSessionOptionResponseDto::getQuestionId));

        return questions.stream()
            .peek(question ->
                question.setOptions(optionsMap.getOrDefault(
                    question.getId(),
                    Collections.emptyList()
                ))).toList();
    }

}
