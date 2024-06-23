package top.frankxxj.homework.api.videobackend.likeview;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link ViewRecord}
 */
public record ViewRecordDto(UUID videoId) implements Serializable {
}