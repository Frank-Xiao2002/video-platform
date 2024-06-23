package top.frankxxj.homework.api.videobackend.security.user;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.frankxxj.homework.api.videobackend.security.role.SecurityRole;

import java.util.Collection;

@ToString
@Getter
public class SecurityUser implements UserDetails {
    private final User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getUserRoleRelations().stream()
                .map(userRoleRelation -> new SecurityRole(userRoleRelation.getRole())).toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

}
