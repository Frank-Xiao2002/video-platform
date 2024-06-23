package top.frankxxj.homework.api.videobackend.vod;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicVODController {
    private final BasicVodService basicVodService;

    public BasicVODController(BasicVodService basicVodService) {
        this.basicVodService = basicVodService;
    }

}
