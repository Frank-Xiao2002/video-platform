package top.frankxxj.homework.api.videobackend.security.userrole;

import org.springframework.data.domain.Pageable;
import top.frankxxj.homework.api.videobackend.security.role.Role;

import java.util.List;
import java.util.UUID;

public interface UserRoleRelationService {
    void assignRole(UUID userId, UUID roleId);

    void removeRole(UUID userId, UUID roleId);

    List<Role> getRoles(UUID userId, Pageable pageable);
}
