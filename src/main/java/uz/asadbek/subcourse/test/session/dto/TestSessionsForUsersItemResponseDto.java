package uz.asadbek.subcourse.test.session.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestSessionsForUsersItemResponseDto {
    private String lastName;
    private String firstName;
    private String email;
    private Integer score;
    private Integer maxScore;
    private Integer remainingSeconds;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private TestSessionStatus status;
}
