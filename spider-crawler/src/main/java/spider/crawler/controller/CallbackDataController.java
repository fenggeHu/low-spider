package spider.crawler.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;
import spider.crawler.dto.Item;

/**
 * @author max.hu  @date 2024/12/24
 **/
@Slf4j
@RestController
@RequestMapping("/callback")
public class CallbackDataController {

    @CrossOrigin("*")
    @PostMapping("/jin")
    public String post(@RequestParam("url") String url,     // target url
                       @RequestParam("name") String name,
                       @RequestParam("html") String html) {
//        log.info("url={}, name={}", url, name);
        if (!"class".equals(name)) {
            return "class is " + name;
        }
        if (StringUtils.isBlank(html)) {
            return "target is null";
        }
        Document doc = Jsoup.parse(html);
//        Elements unlock = doc.getElementsByClass("unlock-flash-btn");
//        if (null != unlock && unlock.size() > 0) {
//            return "VIP";
//        }

        Elements content = doc.getElementsByClass("flash-text");
        if (null == content || content.size() == 0) {
            return "flash-text is null";
        }
        Item item = new Item();
        item.setTarget(url);
        item.setName(name);
        item.setContent(content.get(0).text());
        Elements title = doc.getElementsByClass("right-common-title");
        if (null != title && title.size() > 0) {
            item.setTitle(title.get(0).text());
        }
        Elements important = doc.getElementsByClass("is-important");
        if (null != important && important.size() > 0) {
            item.setImportant(true);
        }

        log.info("item={}", item);
        return name;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
