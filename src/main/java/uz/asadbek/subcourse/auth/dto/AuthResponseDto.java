package uz.asadbek.subcourse.auth.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.asadbek.subcourse.user.dto.UserResponseDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String bearerToken;
    private LocalDateTime expiresIn;
    private UserResponseDto user;
}
