package uz.asadbek.subcourse.test.dto;

import java.util.List;
import uz.asadbek.subcourse.test.question.dto.TestQuestionRequestDto;

public class TestRequestDto {
    private String title;
    private List<TestQuestionRequestDto> questions;
}
