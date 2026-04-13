package uz.asadbek.subcourse.science.dto;

import lombok.Data;
import uz.asadbek.subcourse.util.dto.DescriptionDto;
import uz.asadbek.subcourse.util.dto.NameDto;

@Data
public class ScienceRequestDto {
    private NameDto name;
    private DescriptionDto description;
}
