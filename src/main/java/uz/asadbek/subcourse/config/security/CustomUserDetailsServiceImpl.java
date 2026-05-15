package uz.asadbek.subcourse.config.security;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.asadbek.subcourse.auth.dto.AuthProviderEnum;
import uz.asadbek.subcourse.exception.BadRequestException;
import uz.asadbek.subcourse.exception.NotFoundException;
import uz.asadbek.subcourse.user.UserRepository;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public @NotNull UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username)
            .orElseThrow(
                () -> ExceptionUtil.build(NotFoundException.class, "error.auth.user_not_found",
                    username));

        if (user.getProvider().equals(AuthProviderEnum.GOOGLE)) {
            throw ExceptionUtil.build(BadRequestException.class, "error.auth.provider_not_allowed");
        }
        if (!user.isEnabled()) {
            throw ExceptionUtil.build(BadRequestException.class, "error.auth.user_disabled");
        }
        return CustomUserDetails.builder()
            .user(user)
            .id(user.getId())
            .build();
    }

    public UserDetails loadGoogleUser(String email) {
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> ExceptionUtil.build(NotFoundException.class,"error.auth.user_not_found", email));
        return new CustomUserDetails(user, user.getId());
    }
}
