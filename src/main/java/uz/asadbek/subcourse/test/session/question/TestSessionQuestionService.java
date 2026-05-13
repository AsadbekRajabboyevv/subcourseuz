package uz.asadbek.subcourse.test.session.question;

import java.util.List;
import uz.asadbek.subcourse.test.question.TestQuestionEntity;
import uz.asadbek.subcourse.test.session.question.dto.TestSessionQuestionResponseDto;

public interface TestSessionQuestionService {

    void initializeSessionQuestions(Long sessionId, List<TestQuestionEntity> questions);
    List<TestSessionQuestionResponseDto> findBySessionId(Long sessionId);
}
