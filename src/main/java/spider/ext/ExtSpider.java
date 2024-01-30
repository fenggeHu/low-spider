package spider.ext;

import lombok.Getter;
import lombok.Setter;
import spider.Spider;

/**
 * 扩展spider功能
 *
 * @author max.hu  @date 2024/01/23
 **/
public class ExtSpider {
    @Getter
    @Setter
    private Spider spider;
    /**
     * 有些spider针对同一个uri/api，仅仅参数变化
     */
    @Getter
    @Setter
    private String uri;

    public static ExtSpider of(Spider spider, String uri) {
        ExtSpider ret = new ExtSpider();
        ret.setSpider(spider);
        ret.setUri(uri);
        return ret;
    }
}
