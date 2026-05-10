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
    private Boolean finished;
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
        Boolean finished
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
        this.finished = finished;
    }
}
