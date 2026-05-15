package uz.asadbek.subcourse.test.test.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestReviewOptionDto {
    private Long id;
    private String text;
    private String imagePath;
}
