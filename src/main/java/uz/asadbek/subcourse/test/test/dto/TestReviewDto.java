package uz.asadbek.subcourse.test.test.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TestReviewDto {
    private String testName;
    private String testDescription;
    private Double score;
    private Double maxScore;
    private Integer correctAnswers;
    private Integer totalQuestions;
    private String testImagePath;
    private String testLang;
    private String testScience;
    private String testGrade;
    private String testLesson;
    private String testCourse;
    private String testAuthor;
    private String testDuration;
    private String spentTime;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private List<TestReviewQuestionDto> questions;
}
