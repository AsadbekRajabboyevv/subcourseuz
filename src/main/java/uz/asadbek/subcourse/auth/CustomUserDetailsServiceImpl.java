package uz.asadbek.subcourse.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.asadbek.subcourse.exception.NotFoundException;
import uz.asadbek.subcourse.user.UserRepository;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username)
            .orElseThrow(
                () -> ExceptionUtil.build(NotFoundException.class, "error.auth.user_not_found",
                    username));

        return CustomUserDetails.builder()
            .user(user)
            .build();
    }

}
