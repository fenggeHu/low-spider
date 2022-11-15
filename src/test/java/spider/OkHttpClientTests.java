package spider;

import org.junit.Assert;
import org.junit.Test;
import spider.handler.Handler;
import spider.handler.HttpClientHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jinfeng.hu  @Date 2022/11/15
 **/
public class OkHttpClientTests {

    @Test
    public void testHttpClientHandler() {
        Map<String, String> header = new HashMap<String, String>() {{
            put("accept", "application/json");
            put("content-type", "application/json");
        }};
        Handler handler = new HttpClientHandler(header);

        Spider spider = Spider.of().use(handler);
        Object o = spider.get("https://github.com/square/okhttp");
        Assert.assertNotNull(o);
    }
}
