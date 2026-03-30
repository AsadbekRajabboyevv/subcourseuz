package uz.asadbek.subcourse.test.dto;

import lombok.Getter;

@Getter
public class SubmitAnswerRequestDto {
    private Long sessionId;
    private Long questionId;
    private Long optionId;
}
