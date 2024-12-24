package spider.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;
import spider.crawler.bean.Item;

/**
 * @author max.hu  @date 2024/12/24
 **/
@RestController
@RequestMapping("/callback")
public class CallbackDataController {

    @CrossOrigin("*")
    @PostMapping("/post")
    public String post(@RequestParam("url") String url,
                       @RequestParam("name") String name,
                       @RequestParam("target") String target) {
        if (StringUtils.isBlank(target)) {
            return "target is null";
        }
        Document doc = Jsoup.parse(target);
        Elements title = doc.getElementsByClass("right-common-title");
        if (null == title || title.size() == 0) {
            return "right-common-title is null";
        }
        Elements unlock = doc.getElementsByClass("unlock-flash-btn");
        if (null != unlock) {
            return "VIP";
        }

        Elements content = doc.getElementsByClass("flash-text");
        if (null == content || content.size() == 0) {
            return "flash-text is null";
        }

        Item item = new Item();
        item.setUrl(url);
        item.setName(name);
        item.setTitle(title.get(0).text());
        item.setContent(content.get(0).text());

        System.out.println(item);
        return name;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
