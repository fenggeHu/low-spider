package spider.crawler.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spider.utils.JacksonUtil;

import java.util.Map;

/**
 * @author max.hu  @date 2024/12/24
 **/
@RestController
@RequestMapping("/callback")
public class CallbackDataController {

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
