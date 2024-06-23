package top.frankxxj.homework.api.videobackend.security.user;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record UserDto(String username,
                      String password) implements Serializable {
}