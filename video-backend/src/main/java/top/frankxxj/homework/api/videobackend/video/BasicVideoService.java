package top.frankxxj.homework.api.videobackend.video;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.frankxxj.homework.api.videobackend.likeview.LikeRecord;
import top.frankxxj.homework.api.videobackend.likeview.LikeRecordRepository;
import top.frankxxj.homework.api.videobackend.likeview.ViewRecordRepository;
import top.frankxxj.homework.api.videobackend.security.user.SecurityUser;
import top.frankxxj.homework.api.videobackend.security.user.User;
import top.frankxxj.homework.api.videobackend.vod.AuthUrlDemo;
import top.frankxxj.homework.api.videobackend.vod.BasicVodService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicVideoService implements VideoService {
    private final ViewRecordRepository viewRecordRepository;
    private final LikeRecordRepository likeRecordRepository;
    private final BasicVodService basicVodService;
    private final VideoRepository videoRepository;
    private final Environment environment;

    @Override
    public boolean likeVideo(final Video video, final User user) {
        AtomicBoolean result = new AtomicBoolean(false);
        likeRecordRepository.findByUser_IdAndVideo_Id(user.getId(), video.getId()).ifPresentOrElse(
                likeRecord -> {
                    likeRecordRepository.delete(likeRecord);
                    log.info("Deleted user {} 's like for video {}", user.getId(), video.getId());
                    result.set(false);
                    Runnable runnable = () -> modifyLikes(video, false);
                    runnable.run();
                },
                () -> {
                    likeRecordRepository.save(new LikeRecord(null, user, video));
                    log.info("Saved user {} 's like for video {}", user.getId(), video.getId());
                    result.set(true);
                    Runnable runnable = () -> modifyLikes(video, true);
                    runnable.run();
                });
        return result.get();
    }

    private synchronized void modifyLikes(Video video, final boolean isIncrement) {
        video = videoRepository.findById(video.getId())
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));
        if (isIncrement) {
            video.setLikes(video.getLikes() + 1);
        } else {
            video.setLikes(video.getLikes() - 1);
        }
        videoRepository.save(video);
        log.info("Modified likes for video {} - isIncrement: {}", video.getId(), isIncrement);
    }

    @Override
    public UUID findNextVideoForUser(final UUID userid) {
        List<Video> videos = videoRepository.findAll(Sort.by(Sort.Direction.DESC, "likes"));
        for (Video video : videos) {
            if (viewRecordRepository.existsByUser_IdAndVideo_Id(userid, video.getId())) {
                continue;
            }
            return video.getId();
        }
        return null;
    }

    @Override
    public String findNextVideoURLForUser(final UUID vid) {
        if (vid == null)
            return null;
        var response = basicVodService.showAssetMeta(vid.toString().replace("-", ""));
        var url = response.getAssetInfoArray().get(0).getBaseInfo().getVideoUrl();
        log.info("Found video url: {}", url);
        url = AuthUrlDemo.createAuthInfoUrlByAlgorithmA(url, environment.getProperty("vod.private_key"));
        log.info("Generated url with key protection: {}", url);
        return url;
    }

    @Override
    public void deleteVideo(UUID vid) {
        videoRepository.deleteById(vid);
        log.info("Deleted video {}", vid);
    }

    @Override
    public void updateVideo(UUID assetId, VideoUpdateDto updateDTO) {
        videoRepository.findById(assetId).ifPresentOrElse(video -> {
                    log.info("Updating {} ...", video);
                    video.setTitle(updateDTO.title());
                    video.setDescription(updateDTO.description());
                    video.setLatestUpdateTime(LocalDateTime.now());
                    videoRepository.save(video);
                    log.info("Updated {} successfully", video);
                },
                () -> {
                    throw new IllegalArgumentException("Video not found");
                });
    }

    @Override
    public List<Video> listVideos(Pageable pageable) {
        var userId = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();
        return videoRepository.findByCreator_Id(userId, pageable);
    }
}
