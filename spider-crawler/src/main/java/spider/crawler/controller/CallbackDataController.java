package spider.crawler.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author max.hu  @date 2024/12/24
 **/
@RestController
@RequestMapping("/callback")
public class CallbackDataController {

    @CrossOrigin("*")
    @PostMapping("/post")
    public boolean post(@RequestParam("url") String url,
                        @RequestParam("name") String name,
                        @RequestParam("target") String target) {
        System.out.println(target);
        return true;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
