package uz.asadbek.subcourse.test.session.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTestSessionResponseDto {
    private Long sessionId;
    private Long testId;
    private String testName;
    private TestSessionStatus status;
    private Integer correctAnswers;
    private Integer wrongAnswers;
    private Double score;
    private Integer maxScore;
    private Long remainingSeconds;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    public UserTestSessionResponseDto(
        Long sessionId,
        Long testId,
        String testName,
        TestSessionStatus status,
        Integer correctAnswers,
        Integer wrongAnswers,
        Double score,
        Integer maxScore,
        LocalDateTime startedAt,
        LocalDateTime finishedAt
    ) {
        this.sessionId = sessionId;
        this.testId = testId;
        this.testName = testName;
        this.status = status;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.score = score;
        this.maxScore = maxScore;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        if (startedAt != null && finishedAt != null) {
            this.remainingSeconds = java.time.Duration.between(startedAt, finishedAt).getSeconds();
        } else if (startedAt != null) {
            this.remainingSeconds = java.time.Duration.between(startedAt, LocalDateTime.now()).getSeconds();
        } else {
            this.remainingSeconds = 0L;
        }
    }
}
