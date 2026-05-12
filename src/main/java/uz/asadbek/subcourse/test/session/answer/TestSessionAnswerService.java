package uz.asadbek.subcourse.test.session.answer;

import java.util.List;

public interface TestSessionAnswerService {

    void processAnswer(Long sessionId, Long questionId, Long optionId);

    List<TestSessionAnswerEntity> findBySessionId(Long sessionId);
}
