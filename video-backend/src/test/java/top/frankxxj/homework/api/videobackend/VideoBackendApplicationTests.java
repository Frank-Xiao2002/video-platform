package top.frankxxj.homework.api.videobackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.frankxxj.homework.api.videobackend.vod.BasicIAMService;
import top.frankxxj.homework.api.videobackend.vod.BasicVodService;

@SpringBootTest
class VideoBackendApplicationTests {

    @Autowired
    private BasicIAMService basicIAMService;
    @Autowired
    private BasicVodService basicVodService;

    @Test
    void contextLoads() {
    }

//    @Test
//    void getToken() {
//        var token = basicIAMService.getToken();
//        System.out.println(token);
//
//    }

    @Test
    void vod1() {
        var token = basicIAMService.getToken();
        System.out.println("token = " + token);
    }
}
