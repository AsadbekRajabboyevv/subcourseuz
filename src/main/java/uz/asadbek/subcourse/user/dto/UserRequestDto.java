package uz.asadbek.subcourse.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotNull
    @Email
    @UserEmailUnique
    private String email;

    @NotNull
    @Size(max = 20)
    private String password;

    private String bio;

    @NotNull
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    private LocalDate birthDate;

    private String phone;

    private UserPositions position;

}
