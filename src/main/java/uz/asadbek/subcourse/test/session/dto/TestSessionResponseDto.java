package uz.asadbek.subcourse.test.session.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.asadbek.subcourse.test.session.question.dto.TestSessionQuestionResponseDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestSessionResponseDto {

    private Long id;
    private Long testId;
    private String testName;
    private String testDescription;
    private String imagePath;
    private TestSessionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime finishedAt;
    private Long remainingSeconds;
    private String author;
    private String lang;
    private String science;
    private String grade;
    private Boolean finished;
    private Integer maxScore;
    private List<TestSessionQuestionResponseDto> questions;

    public TestSessionResponseDto(
        Long id,
        Long testId,
        String testName,
        String testDescription,
        String imagePath,
        TestSessionStatus status,
        LocalDateTime startedAt,
        LocalDateTime expiresAt,
        LocalDateTime finishedAt,
        String author,
        String lang,
        String science,
        String grade,
        Boolean finished,
        Integer maxScore
    ) {
        this.id = id;
        this.testId = testId;
        this.testName = testName;
        this.testDescription = testDescription;
        this.imagePath = imagePath;
        this.status = status;
        this.startedAt = startedAt;
        this.expiresAt = expiresAt;
        this.finishedAt = finishedAt;
        this.author = author;
        this.lang = lang;
        this.science = science;
        this.grade = grade;
        this.finished = finished;
        this.maxScore = maxScore;
    }
}
