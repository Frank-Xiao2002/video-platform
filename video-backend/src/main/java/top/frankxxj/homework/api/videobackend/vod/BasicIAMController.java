package top.frankxxj.homework.api.videobackend.vod;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicIAMController {
    private final BasicIAMService basicIAMService;

    public BasicIAMController(BasicIAMService basicIAMService) {
        this.basicIAMService = basicIAMService;
    }

    @GetMapping("/iam/token")
    public String getToken(){
        return basicIAMService.getToken();
    }
}
