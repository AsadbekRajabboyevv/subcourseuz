package uz.asadbek.subcourse.test.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestSessionOptionDto {

    private Long optionId;
    private String text;
    private String imageUrl;
}
