package top.frankxxj.homework.api.videobackend.views;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import top.frankxxj.homework.api.videobackend.security.user.SecurityUser;
import top.frankxxj.homework.api.videobackend.video.Video;
import top.frankxxj.homework.api.videobackend.video.VideoRepository;
import top.frankxxj.homework.api.videobackend.video.VideoService;

import java.util.Objects;

@Slf4j
@Controller
public class MyRouter {
    private final VideoRepository videoRepository;
    private final VideoService videoService;

    public MyRouter(VideoRepository videoRepository, VideoService videoService) {
        this.videoRepository = videoRepository;
        this.videoService = videoService;
    }

    @RequestMapping(value = "/watch")
    public String watch(Model model) {
        var uid = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();
        var vid = videoService.findNextVideoForUser(uid);
        if (Objects.nonNull(vid)) {
            var url = videoService.findNextVideoURLForUser(vid);
            Video v = videoRepository.findById(vid).orElse(null);
            model.addAttribute("url", url);
            model.addAttribute("video", v);
            return "watch";
        } else {
            return "no_video_to_watch";
        }
    }

    @RequestMapping(value = "/upload")
    public String upload() {
        return "upload";
    }

    @RequestMapping(value = "/register")
    public String register() {
        return "register";
    }

    @RequestMapping(value = "/list")
    public String list(Model model) {
        var user = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUser();
        var videos = videoRepository.findByCreator_IdOrderByCreateTimeDesc(user.getId());
//        List<Video> videos = videoService.listVideos(Pageable.ofSize(10).first());
        model.addAttribute("videos", videos);
        model.addAttribute("currentPage", 1);
        return "video_list";
    }
}
