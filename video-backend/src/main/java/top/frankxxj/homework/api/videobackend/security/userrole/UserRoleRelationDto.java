package top.frankxxj.homework.api.videobackend.security.userrole;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link UserRoleRelation}
 */
public record UserRoleRelationDto(UUID userId,
                                  UUID roleId) implements Serializable {
}