package uz.asadbek.subcourse.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.auth.dto.AuthProviderEnum;
import uz.asadbek.subcourse.exception.BadRequestException;
import uz.asadbek.subcourse.user.UserEntity;
import uz.asadbek.subcourse.user.UserService;
import uz.asadbek.subcourse.user.dto.UserPositions;
import uz.asadbek.subcourse.user.dto.UserRoles;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        var attributes = oAuth2User.getAttributes();

        var email      = (String) attributes.get("email");
        var name       = (String) attributes.getOrDefault("name", "");
        var givenName  = (String) attributes.getOrDefault("given_name", "");
        var familyName = (String) attributes.getOrDefault("family_name", "");
//        var picture    = (String) attributes.getOrDefault("picture", null);

        if (email == null || email.isBlank()) {
            throw ExceptionUtil.build(BadRequestException.class, "auth.google_email_missing");
        }

        var userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            var existingUser = userOpt.get();
            if (existingUser.getProvider() != AuthProviderEnum.GOOGLE) {
                throw ExceptionUtil.build(BadRequestException.class, "auth.local_account_exists");
            }

            userRepository.save(existingUser);
            return new DefaultOAuth2User(oAuth2User.getAuthorities(), attributes, "email");
        }

        var newUser = new UserEntity();
        newUser.setEmail(email);
        newUser.setFirstName(!givenName.isBlank() ? givenName : name);
        newUser.setLastName(familyName);
        newUser.setProvider(AuthProviderEnum.GOOGLE);
        newUser.setRole(UserRoles.ROLE_USER.name());
        newUser.setEnabled(true);
        userRepository.save(newUser);

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), attributes, "email");
    }
}
