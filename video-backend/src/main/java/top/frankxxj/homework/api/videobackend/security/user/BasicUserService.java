package top.frankxxj.homework.api.videobackend.security.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.frankxxj.homework.api.videobackend.security.role.Role;
import top.frankxxj.homework.api.videobackend.security.role.RoleRepository;
import top.frankxxj.homework.api.videobackend.security.userrole.UserRoleRelation;
import top.frankxxj.homework.api.videobackend.security.userrole.UserRoleRelationRepository;

import java.util.Objects;

@Service
@Slf4j
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserRoleRelationRepository userRoleRelationRepository;
    private final Role userRole;

    public BasicUserService(UserRepository userRepository,
                            PasswordEncoder encoder,
                            UserRoleRelationRepository userRoleRelationRepository,
                            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userRoleRelationRepository = userRoleRelationRepository;
        this.userRole = roleRepository.findByName("USER").orElseGet
                (() -> roleRepository.save(new Role(null, "USER", null)));
    }

    @Override
    public User create(UserDto userDto) {
        log.info("Creating user: {}", userDto.username());
        var user = new User();
        user.setUsername(userDto.username().trim());
        user.setPassword(encoder.encode(userDto.password()));
        try {
            userRepository.save(user);
            log.info("User created: {}", user);
            userRoleRelationRepository.save(new UserRoleRelation(null, user, userRole));
            log.info("Add role USER to user: {}", user);
        } catch (Exception e) {
            log.warn("User already exists: {}", user);
            user = null;
        }
        return user;
    }

    @Override
    public void updatePassword(UserDto userDto) {
        var u = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Assert.isTrue(Objects.equals(u.getUsername(), userDto.username()),
                "One can only update his own password");
        var user = userRepository.findByUsername(userDto.username())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPassword(encoder.encode(userDto.password()));
        userRepository.save(user);
        log.info("Password updated for user: {}", user);
    }

    @Override
    public Boolean checkUsername(String username) {
        return userRepository.existsByUsername(username.trim());
    }

    @Override
    public Boolean existAnyUser() {
        return userRepository.count() > 0;
    }
}
