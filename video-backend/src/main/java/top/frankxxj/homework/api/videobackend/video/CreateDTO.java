package top.frankxxj.homework.api.videobackend.video;

import java.io.Serializable;

/**
 * DTO for {@link Video}
 */
public record CreateDTO(String title,
                        String filename,
                        String description) implements Serializable {
}