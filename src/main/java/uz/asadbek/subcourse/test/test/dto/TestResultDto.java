package uz.asadbek.subcourse.test.test.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TestResultDto {

    private Double score;
    private Integer correctAnswers;
    private Integer totalQuestions;
    private Long sessionId;
    private String spentTime;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
}
