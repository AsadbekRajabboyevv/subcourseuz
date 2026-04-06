package uz.asadbek.subcourse.user;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.auth.CustomUserDetails;
import uz.asadbek.subcourse.auth.dto.AuthRequestDto;
import uz.asadbek.subcourse.auth.dto.AuthResponseDto;
import uz.asadbek.subcourse.user.dto.UserRequestDto;
import uz.asadbek.subcourse.user.dto.UserResponseDto;
import uz.asadbek.subcourse.user.dto.UserRoles;
import uz.asadbek.subcourse.user.filter.UserFilter;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

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
            throw ExceptionUtil.badRequestException("email_already_exists");
        }

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto update(Long id, UserRequestDto dto) {

        var user = userRepository.findById(id)
            .orElseThrow(() -> ExceptionUtil.notFoundException("user_not_found"));

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
    public boolean emailExists(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    @Transactional
    public boolean confirmUser(String token) {
        return userRepository.findByConfirmationToken(token)
            .map(user -> {
                user.setEnabled(true);
                user.setConfirmationToken(null);
                userRepository.save(user);
                return true;
            })
            .orElseGet(() -> {
                log.error("User not found for token: {}", token);
                return false;
            });
    }

    @Override
    public String getConfirmationToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    @Transactional
    public void changeEmail(String newEmail) {

        var user = getCurrentUser();

        if (user.getEmail().equalsIgnoreCase(newEmail)) {
            return;
        }

        if (emailExists(newEmail)) {
            throw ExceptionUtil.badRequestException("email_already_exists");
        }

        user.setEmail(newEmail);
        user.setEnabled(false);

        var token = UUID.randomUUID().toString();
        user.setConfirmationToken(token);

        userRepository.save(user);

        var confirmationLink =
            STR."http://localhost:8080/v1/api/users/confirm?token=\{token}";

        var body = buildChangeEmailTemplate(confirmationLink);

        emailService.sendEmail(
            newEmail,
            "Emailni tasdiqlash",
            body
        );
    }


    @Override
    @Transactional
    public AuthResponseDto register(UserRequestDto dto, String language) {

        if (emailExists(dto.getEmail())) {
            log.error("Email already exists: {}", dto.getEmail());
            throw ExceptionUtil.badRequestException("email_already_exists");
        }

        var user = userMapper.toEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(UserRoles.ROLE_USER.name());
        user.setEnabled(false);

        var token = UUID.randomUUID().toString();
        user.setConfirmationToken(token);
        user.setLanguage(language);

        user = userRepository.save(user);

        var confirmationLink =
            STR."http://localhost:8080/v1/api/auth/confirm?token=\{token}";

        var body = buildEmailTemplate(confirmationLink);

        emailService.sendEmail(
            user.getEmail(),
            "Email tasdiqlash",
            body
        );

        return AuthResponseDto.builder()
            .bearerToken(null)
            .expiresIn(null)
            .user(userMapper.toDto(user))
            .build();
    }

    @Override
    @Transactional
    public AuthResponseDto login(AuthRequestDto authRequestDto, String language) {

        var user = userRepository.findByEmail(authRequestDto.getEmail())
            .orElseThrow(() -> ExceptionUtil.badRequestException("invalid_credentials"));

        if (!user.isEnabled()) {
            throw ExceptionUtil.badRequestException("user_not_enabled");
        }

        if (!passwordEncoder.matches(authRequestDto.getPassword(), user.getPassword())) {
            throw ExceptionUtil.badRequestException("invalid_credentials");
        }

        var userDetails = CustomUserDetails.builder()
            .user(user)
            .language(language)
            .build();

        return AuthResponseDto.builder()
            .bearerToken(jwtUtil.generateAccessToken(userDetails))
            .expiresIn(LocalDateTime.now()
                .plusSeconds(jwtUtil.getAccessTokenExpirationMs() / 1000))
            .user(userMapper.toDto(user))
            .build();
    }

    @Override
    public Long count() {
        return userRepository.count();
    }

    private UserEntity getCurrentUser() {
        var username = SecurityContextHolder.
            getContext().getAuthentication().getName();
        return userRepository.findByEmail(username)
            .orElseThrow(() -> ExceptionUtil
                .notFoundException("user_not_found"));
    }
    private String buildEmailTemplate(String confirmationLink) {
        return """
            <html>
                <body style="font-family: Arial, sans-serif; background-color:#f6f6f6; padding:20px;">
                    <div style="max-width:600px;margin:auto;background:white;padding:20px;border-radius:10px;">
                        <h2 style="color:#2c3e50;">Assalomu alaykum 👋</h2>
                        <p>Ro‘yxatdan o‘tganingiz uchun rahmat.</p>
                        <p>Akkauntingizni faollashtirish uchun quyidagi tugmani bosing:</p>
                        <div style="text-align:center;margin:30px 0;">
                            <a href="%s"
                               style="background:#3498db;color:white;padding:12px 24px;
                               text-decoration:none;border-radius:6px;font-weight:bold;">
                                Akkauntni tasdiqlash
                            </a>
                        </div>
                        <p style="font-size:12px;color:#888;">
                            Agar tugma ishlamasa, quyidagi linkni ishlating:
                        </p>
                        <p style="word-break:break-all;">
                            <a href="%s">%s</a>
                        </p>
                        <hr>
                        <p style="font-size:12px;color:#aaa;">
                            Bu avtomatik yuborilgan xabar.
                        </p>
                    </div>
                </body>
            </html>
        """.formatted(confirmationLink, confirmationLink, confirmationLink);
    }
    private String buildChangeEmailTemplate(String confirmationLink) {
        return """
        <html>
            <body style="font-family: Arial, sans-serif; background:#f6f6f6; padding:20px;">
                <div style="max-width:600px;margin:auto;background:white;padding:20px;border-radius:10px;">
                    <h2 style="color:#2c3e50;">Email o‘zgartirildi 🔄</h2>
                    <p>Siz emailingizni o‘zgartirdingiz.</p>
                    <p>Yangi emailni tasdiqlash uchun quyidagi tugmani bosing:</p>
                    <div style="text-align:center;margin:30px 0;">
                        <a href="%s"
                           style="background:#27ae60;color:white;padding:12px 24px;
                           text-decoration:none;border-radius:6px;font-weight:bold;">
                            Emailni tasdiqlash
                        </a>
                    </div>
                    <p style="font-size:12px;color:#888;">
                        Agar tugma ishlamasa, quyidagi linkni ishlating:
                    </p>
                    <p style="word-break:break-all;">
                        <a href="%s">%s</a>
                    </p>
                    <hr>
                    <p style="font-size:12px;color:#aaa;">
                        Agar bu siz bo‘lmasangiz, iltimos e’tiborsiz qoldiring.
                    </p>
                </div>
            </body>
        </html>
    """.formatted(confirmationLink, confirmationLink, confirmationLink);
    }
}
