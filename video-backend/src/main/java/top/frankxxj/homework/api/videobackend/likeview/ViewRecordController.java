package top.frankxxj.homework.api.videobackend.likeview;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.frankxxj.homework.api.videobackend.security.user.SecurityUser;
import top.frankxxj.homework.api.videobackend.security.user.User;
import top.frankxxj.homework.api.videobackend.video.Video;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/view-record")
@RequiredArgsConstructor
public class ViewRecordController {

    private final ViewRecordRepository viewRecordRepository;

    @PostMapping
    public ResponseEntity<?> addRecord(@RequestBody ViewRecordDto viewRecordDto) {
        var userId = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();
        viewRecordRepository.save(ViewRecord.builder()
                .viewTime(LocalDateTime.now())
                .user(new User(userId))
                .video(new Video(viewRecordDto.videoId())).build());
        return ResponseEntity.ok().build();
    }

}
