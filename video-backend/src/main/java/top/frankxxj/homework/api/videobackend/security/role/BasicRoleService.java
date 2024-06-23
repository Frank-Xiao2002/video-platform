package top.frankxxj.homework.api.videobackend.security.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicRoleService implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role create(String roleName) {
        var role = new Role();
        role.setName(roleName.trim().toUpperCase());
        return roleRepository.save(role);
    }

    @Override
    public void delete(String roleName) {
        roleRepository.deleteByName(roleName.trim().toUpperCase());
    }

}
