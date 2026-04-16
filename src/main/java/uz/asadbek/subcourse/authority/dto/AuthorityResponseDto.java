package uz.asadbek.subcourse.authority.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.asadbek.base.dto.BaseAuditResponseDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthorityResponseDto extends BaseAuditResponseDto {

    private Long id;
    private String name;
}
