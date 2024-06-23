package top.frankxxj.homework.api.videobackend.security.role;

public interface RoleService {
    Role create(String roleName);

    void delete(String roleName);

}
