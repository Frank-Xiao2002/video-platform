package top.frankxxj.homework.api.videobackend.security.userrole;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/userrole")
@RequiredArgsConstructor
public class UserRoleRelationController {
    private final UserRoleRelationService userRoleRelationService;

    @PostMapping
    public ResponseEntity<?> assignRole(@RequestBody UserRoleRelationDto userRoleRelationDto) {
        userRoleRelationService.assignRole(userRoleRelationDto.userId(), userRoleRelationDto.roleId());
        return ResponseEntity.ok("Role assigned successfully");
    }

    @DeleteMapping
    public ResponseEntity<?> removeRole(@RequestBody UserRoleRelationDto userRoleRelationDto) {
        userRoleRelationService.removeRole(userRoleRelationDto.userId(), userRoleRelationDto.roleId());
        return ResponseEntity.ok("Role removed successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getRoles(@PathVariable UUID userId, Pageable pageable) {
        return ResponseEntity.ok(userRoleRelationService.getRoles(userId, pageable));
    }
}
