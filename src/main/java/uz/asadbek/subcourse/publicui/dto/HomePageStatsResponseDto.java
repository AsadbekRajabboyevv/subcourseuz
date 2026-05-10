package uz.asadbek.subcourse.publicui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomePageStatsResponseDto {
    private Long coursesCount;
    private Long usersCount;
    private Long videoCoursesCount;
    private Long testsCount;
}
