package uz.asadbek.subcourse.test.session.option.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestSessionOptionResponseDto {

    private Long id;
    private String text;
    private String imagePath;
    @JsonIgnore
    private Long questionId;
}
