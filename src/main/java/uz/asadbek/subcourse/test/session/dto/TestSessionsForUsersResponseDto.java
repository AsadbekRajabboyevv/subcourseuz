package uz.asadbek.subcourse.test.session.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestSessionsForUsersResponseDto {
    private Long testId;
    private String testName;
    private Integer maxScore;
    private String author;
    private String lang;
    private TestSessionsForUsersItemResponseDto users;
}
