package spider.ext;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import spider.Spider;
import spider.base.Context;
import spider.base.HttpMethod;
import spider.handler.DynamicGsonHandler;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 扩展spider功能
 * 1，保留原始的url
 * 2，公用spider实例时，返回值不一样和泛型的处理
 *
 * @author max.hu  @date 2024/01/23
 **/
@Slf4j
public class ExtSpider {
    @Getter
    @Setter
    private Spider spider;
    /**
     * 有些spider针对同一个uri/api，仅仅参数变化
     */
    @Getter
    private String uri;

    public ExtSpider setUri(String uri) {
        this.uri = uri;
        return this;
    }

    // 处理返回的泛型类型
    public Object get(String url, Type type) {
        Context context = Context.get(url)
                .addCustomVar(DynamicGsonHandler.CUSTOM_KEY, type);
        return this.spider.request(context);
    }

    public Object get(String url, Type type, Map<String, Object> params) {
        Context context = Context.of(url, params, HttpMethod.GET)
                .addCustomVar(DynamicGsonHandler.CUSTOM_KEY, type);
        return this.spider.request(context);
    }

    public static ExtSpider of(Spider spider) {
        ExtSpider ret = new ExtSpider();
        ret.setSpider(spider);
        return ret;
    }

    public static ExtSpider of(Spider spider, String uri) {
        ExtSpider ret = new ExtSpider();
        ret.setSpider(spider);
        ret.setUri(uri);
        return ret;
    }

}
