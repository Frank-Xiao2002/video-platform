package top.frankxxj.homework.api.videobackend.security.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    void deleteByName(String name);

    Optional<Role> findByName(String name);
}