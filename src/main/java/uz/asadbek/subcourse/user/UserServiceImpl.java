package uz.asadbek.subcourse.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.exception.NotFoundException;
import uz.asadbek.subcourse.user.dto.UserRequestDto;
import uz.asadbek.subcourse.user.dto.UserResponseDto;
import uz.asadbek.subcourse.user.dto.UserRoles;
import uz.asadbek.subcourse.user.filter.UserFilter;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserResponseDto> get(UserFilter filter, Pageable pageable) {
        return userRepository.get(filter, pageable);
    }

    @Override
    public UserResponseDto get(Long aLong) {
        return userRepository.get(aLong);
    }

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto dto) {

        var user = userMapper.toEntity(dto);

        user.setRole(UserRoles.ROLE_USER.name());
        user.setEnabled(true);

        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            log.error("Email already exists: {}", dto.getEmail());
            throw ExceptionUtil.validationException("email", "error.auth.email_already_exists");
        }

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto update(Long id, UserRequestDto dto) {

        var user = userRepository.findById(id)
            .orElseThrow(() -> ExceptionUtil.build(NotFoundException.class, "error.not_found.user"));

        userMapper.partialUpdate(user, dto);

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void delete(Long aLong) {
        userRepository.deleteById(aLong);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<UserEntity> findByConfirmationToken(String token) {
        return userRepository.findByConfirmationToken(token);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserEntity getCurrentUser() {
        var username = SecurityContextHolder.
            getContext().getAuthentication().getName();
        return userRepository.findByEmail(username)
            .orElseThrow(() -> ExceptionUtil
                .build(NotFoundException.class, "error.auth.user_not_found", username));
    }

    @Override
    public UserEntity findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> ExceptionUtil.build(NotFoundException.class, "error.not_found.user"));
    }

    @Override
    public Long count() {
        return userRepository.count();
    }

}
