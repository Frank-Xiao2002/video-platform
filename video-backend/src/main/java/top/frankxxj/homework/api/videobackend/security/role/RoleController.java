package top.frankxxj.homework.api.videobackend.security.role;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleDto dto) {
        roleService.create(dto.name());
        return ResponseEntity.ok("Role created successfully!");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRole(@RequestBody RoleDto dto) {
        roleService.delete(dto.name());
        return ResponseEntity.ok("Role deleted successfully!");
    }
}
