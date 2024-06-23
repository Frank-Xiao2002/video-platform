package top.frankxxj.homework.api.videobackend.security.userrole;

import org.springframework.data.jpa.repository.JpaRepository;
import top.frankxxj.homework.api.videobackend.security.role.Role;
import top.frankxxj.homework.api.videobackend.security.user.User;

public interface UserRoleRelationRepository extends JpaRepository<UserRoleRelation, Long> {
    void deleteByUserAndRole(User user, Role role);
}