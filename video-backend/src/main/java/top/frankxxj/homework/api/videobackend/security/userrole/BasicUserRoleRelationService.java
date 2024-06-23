package top.frankxxj.homework.api.videobackend.security.userrole;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.frankxxj.homework.api.videobackend.security.role.Role;
import top.frankxxj.homework.api.videobackend.security.user.User;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserRoleRelationService implements UserRoleRelationService {
    private final UserRoleRelationRepository userRoleRelationRepository;

    @Override
    public void assignRole(UUID userId, UUID roleId) {
        userRoleRelationRepository.save(new UserRoleRelation(null, new User(userId), new Role(roleId)));
    }

    @Override
    public void removeRole(UUID userId, UUID roleId) {
        userRoleRelationRepository.deleteByUserAndRole(new User(userId), new Role(roleId));
    }

    @Override
    public List<Role> getRoles(UUID userId, Pageable pageable) {
        var all = userRoleRelationRepository.findAll(Example.of(new UserRoleRelation(null, new User(userId), null)), pageable);
        return all.stream().map(UserRoleRelation::getRole).toList();
    }
}
