package top.frankxxj.homework.api.videobackend.video;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import top.frankxxj.homework.api.videobackend.likeview.ViewRecordDto;
import top.frankxxj.homework.api.videobackend.security.user.SecurityUser;
import top.frankxxj.homework.api.videobackend.vod.BasicVodService;
import top.frankxxj.homework.api.videobackend.vod.ConfirmDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;
    private final BasicVodService basicVodService;

    @PostMapping
    public ResponseEntity<?> createMedia(@RequestBody CreateDTO createDTO) {
        var response = basicVodService.createMediaAsset(createDTO);
        return ResponseEntity.ok(Map.of("upload_url", response.getVideoUploadUrl(), "asset_id", response.getAssetId()));
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmMedia(@RequestBody ConfirmDTO dto) {
        basicVodService.confirmAssetUploadSolution(dto, "CREATED");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/like")
    public ResponseEntity<?> likeVideo(@RequestBody ViewRecordDto dto) {
        var user = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        var video = new Video(dto.videoId());
        var b = videoService.likeVideo(video, user);
        if (b) {
            return ResponseEntity.ok("Add like");
        }
        return ResponseEntity.ok("Remove like");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedia(@PathVariable(name = "id") UUID assetId) {
        videoService.deleteVideo(assetId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedia(@PathVariable(name = "id") UUID assetId, @RequestBody VideoUpdateDto updateDTO) {
        videoService.updateVideo(assetId, updateDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Video>> getVideos(Pageable pageable) {
        log.info("size: {}, pageNo: {}", pageable.getPageSize(), pageable.getPageNumber());
        return ResponseEntity.ok(videoService.listVideos(pageable));
    }
}
