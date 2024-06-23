package top.frankxxj.homework.api.videobackend.video;

import org.springframework.data.domain.Pageable;
import top.frankxxj.homework.api.videobackend.security.user.User;

import java.util.List;
import java.util.UUID;

public interface VideoService {

    boolean likeVideo(Video video, User user);

    UUID findNextVideoForUser(UUID userid);

    String findNextVideoURLForUser(UUID vid);

    void deleteVideo(UUID vid);

    void updateVideo(UUID assetId, VideoUpdateDto updateDTO);

    List<Video> listVideos(Pageable pageable);
}
