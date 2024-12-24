package spider.crawler.controller;

import org.springframework.web.bind.annotation.*;
import spider.utils.JacksonUtil;

import java.util.Map;

/**
 * @author max.hu  @date 2024/12/24
 **/
@RestController
@RequestMapping("/callback")
public class CallbackDataController {

    @CrossOrigin("*")
    @PostMapping("/post")
    public void post(Map<String, Object> data) {
        String json = JacksonUtil.toJson(data);
        System.out.println(json);
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
