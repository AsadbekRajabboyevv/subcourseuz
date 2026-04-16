package uz.asadbek.subcourse.authority.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityRequestDto {

    @NotNull
    @Size(min = 2, max = 50)
    private String name;
}
