package uz.asadbek.subcourse.test.option.dto;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class TestOptionResponseDto {

    private Long id;
    private String text;
    private String imageUrl;
}
