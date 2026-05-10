package uz.asadbek.subcourse.test.test.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestReviewDto {
    private String testName;
    private String testDescription;
    private Double score;
    private Integer correctAnswers;
    private Integer totalQuestions;
    private String testImagePath;
    private String testLang;
    private String testScience;
    private String testGrade;
    private String testLesson;
    private String testCourse;
    private String testAuthor;
    private List<TestReviewQuestionDto> questions;
}
