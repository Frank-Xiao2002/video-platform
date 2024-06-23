package top.frankxxj.homework.api.videobackend.video;

import java.io.Serializable;

/**
 * DTO for {@link Video}
 */
public record VideoUpdateDto(String title,
                             String description) implements Serializable {
}