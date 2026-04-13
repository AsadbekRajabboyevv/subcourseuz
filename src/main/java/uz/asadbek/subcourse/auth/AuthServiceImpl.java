package uz.asadbek.subcourse.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.auth.dto.AuthRequestDto;
import uz.asadbek.subcourse.auth.dto.AuthResponseDto;
import uz.asadbek.subcourse.auth.refresh.RefreshTokenEntity;
import uz.asadbek.subcourse.auth.refresh.RefreshTokenRepository;
import uz.asadbek.subcourse.balance.BalanceService;
import uz.asadbek.subcourse.user.EmailService;
import uz.asadbek.subcourse.user.UserEntity;
import uz.asadbek.subcourse.user.UserMapper;
import uz.asadbek.subcourse.user.UserService;
import uz.asadbek.subcourse.user.dto.UserRequestDto;
import uz.asadbek.subcourse.user.dto.UserRoles;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BalanceService balanceService;

    @Override
    @Transactional
    public AuthResponseDto login(AuthRequestDto dto, String language,
        HttpServletResponse response) {
        UserEntity user = userService.findByEmail(dto.getEmail())
            .orElseThrow(() -> ExceptionUtil.badRequestException("invalid_credentials"));

        if (!user.isEnabled()) {
            throw ExceptionUtil.badRequestException("user_not_enabled");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw ExceptionUtil.badRequestException("invalid_credentials");
        }

        return issueTokens(user, language, response);
    }

    @Override
    @Transactional
    public AuthResponseDto register(UserRequestDto dto, String language) {
        if (userService.emailExists(dto.getEmail())) {
            throw ExceptionUtil.badRequestException("email_already_exists");
        }

        UserEntity user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(UserRoles.ROLE_USER.name());
        user.setEnabled(false);
        user.setLanguage(language);

        String confirmToken = JwtUtil.generateConfirmationToken();
        user.setConfirmationToken(confirmToken);

        user = userService.save(user);
        balanceService.createBalance(user);
        String confirmationLink =
            STR."http://localhost:8080/v1/api/auth/confirm?token=\{confirmToken}";

        emailService.sendEmail(
            user.getEmail(),
            "Email tasdiqlash",
            buildEmailTemplate(confirmationLink)
        );

        return AuthResponseDto.builder()
            .user(userMapper.toDto(user))
            .build();
    }

    @Override
    @Transactional
    public AuthResponseDto refresh(HttpServletRequest request, HttpServletResponse response) {
        String token = JwtUtil.extractRefreshTokenFromCookie(request);

        RefreshTokenEntity stored = refreshTokenRepository.findByToken(token)
            .filter(t -> !t.isRevoked() && t.getExpiryDate().isAfter(LocalDateTime.now()))
            .orElseThrow(() -> ExceptionUtil.badRequestException("refresh_token_expired"));

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        UserEntity user = userService.findById(stored.getUserId());

        return issueTokens(user, user.getLanguage(), response);
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = JwtUtil.extractRefreshTokenFromCookie(request);

        if (token != null) {
            refreshTokenRepository.findByToken(token).ifPresent(t -> {
                t.setRevoked(true);
                refreshTokenRepository.save(t);
            });
        }

        JwtUtil.clearRefreshTokenCookie(response);
    }

    @Override
    @Transactional
    public boolean confirmUser(String token) {
        return userService.findByConfirmationToken(token)
            .map(user -> {
                user.setEnabled(true);
                user.setConfirmationToken(null);
                userService.save(user);
                return true;
            })
            .orElse(false);
    }

    @Override
    @Transactional
    public void changeEmail(String newEmail) {
        UserEntity user = userService.getCurrentUser();

        if (user.getEmail().equalsIgnoreCase(newEmail)) {
            return;
        }

        if (userService.emailExists(newEmail)) {
            throw ExceptionUtil.badRequestException("email_already_exists");
        }

        user.setEmail(newEmail);
        user.setEnabled(false);

        String token = UUID.randomUUID().toString();
        user.setConfirmationToken(token);

        userService.save(user);

        String confirmationLink =
            STR."http://localhost:8080/v1/api/users/confirm?token=\{token}";

        emailService.sendEmail(
            newEmail,
            "Emailni tasdiqlash",
            buildChangeEmailTemplate(confirmationLink)
        );
    }

    private AuthResponseDto issueTokens(UserEntity user, String language,
        HttpServletResponse response) {
        String refreshToken = JwtUtil.generateRefreshToken();

        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setToken(refreshToken);
        entity.setUserId(user.getId());
        entity.setExpiryDate(LocalDateTime.now().plusDays(7));
        entity.setRevoked(false);

        refreshTokenRepository.save(entity);

        JwtUtil.setRefreshTokenCookie(response, refreshToken);

        var userDetails = CustomUserDetails.builder()
            .user(user)
            .language(language)
            .build();

        String accessToken = JwtUtil.generateAccessToken(userDetails);

        return AuthResponseDto.builder()
            .bearerToken(accessToken)
            .expiresIn(LocalDateTime.now()
                .plusSeconds(JwtUtil.ACCESS_TOKEN_EXPIRATION_MS / 1000))
            .user(userMapper.toDto(user))
            .build();
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
