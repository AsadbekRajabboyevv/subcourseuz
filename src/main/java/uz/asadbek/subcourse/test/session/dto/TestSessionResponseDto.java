package uz.asadbek.subcourse.test.session.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestSessionResponseDto {

    // 🔹 session info
    private Long sessionId;
    private Long testId;
    private Long userId;

    private TestSessionStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime finishedAt;

    private Integer durationSeconds;
    private Long remainingSeconds; // 🔥 frontend countdown uchun

    // 🔹 progress
    private Integer totalQuestions;
    private Integer answeredQuestions;
    private Integer currentQuestionIndex;

    // 🔹 questions (snapshot)
    private List<TestSessionQuestionResponseDto> questions;

    // 🔹 optional (agar ko‘rsatmoqchi bo‘lsangiz)
    private Boolean finished;
}
