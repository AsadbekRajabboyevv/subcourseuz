package uz.asadbek.subcourse.test.dto;

import lombok.Getter;
import uz.asadbek.subcourse.test.question.TestQuestionEntity;
import uz.asadbek.subcourse.test.session.TestSessionEntity;
import uz.asadbek.subcourse.util.annotation.existindb.ExistsInDb;

@Getter
public class SubmitAnswerRequestDto {

    @ExistsInDb(entity = TestSessionEntity.class)
    private Long sessionId;

    @ExistsInDb(entity = TestQuestionEntity.class)
    private Long questionId;

    @ExistsInDb(entity = TestQuestionEntity.class)
    private Long optionId;
}
