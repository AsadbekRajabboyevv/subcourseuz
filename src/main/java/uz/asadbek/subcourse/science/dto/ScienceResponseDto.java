package uz.asadbek.subcourse.science.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScienceResponseDto {
    private Long id;
    private String name;
    private String description;
    private String imagePath;

}
