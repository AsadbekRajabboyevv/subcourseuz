package uz.asadbek.subcourse.user.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private String position;
    private String phone;
    private LocalDate birthDate;
    private String bio;
}
