package uz.asadbek.subcourse.science.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import uz.asadbek.subcourse.util.dto.DescriptionDto;
import uz.asadbek.subcourse.util.dto.NameDto;

@Data
@AllArgsConstructor
public class OneScienceResponseDto {

    private Long id;
    private NameDto name;
    private DescriptionDto description;
    private String imagePath;
}
