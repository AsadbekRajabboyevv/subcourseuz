package uz.asadbek.subcourse.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.asadbek.subcourse.user.UserEntity;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserEntity user;
    private final Long id;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> granted = new HashSet<>();
        granted.add(user::getRole);
        return granted;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    public Long getId() {
        return this.id == null ? user.getId() : this.id;
    }
}
