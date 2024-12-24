package spider.crawler.controller;

import org.springframework.web.bind.annotation.*;
import spider.crawler.bean.Data;
import spider.utils.JacksonUtil;

/**
 * @author max.hu  @date 2024/12/24
 **/
@RestController
@RequestMapping("/callback")
public class CallbackDataController {

    @CrossOrigin("*")
    @PostMapping("/post")
    public void post(Data data) {
        String json = JacksonUtil.toJson(data);
        System.out.println(json);
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
