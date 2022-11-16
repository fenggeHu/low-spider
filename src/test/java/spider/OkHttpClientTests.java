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
            put("Content-Type", "application/json");
        }};
        HttpClientHandler handler = new HttpClientHandler(header);
//        HttpClientHandler handler = new HttpClientHandler();
        handler.setReadTimeout(30000);

        EmailSendDO params = EmailSendDO.builder()
                .userName("jinfeng.hu")
                .requestId("fq2afl21e")
                .orgId(0L)
                .businessType("cmm")
                .email("jinfeng.hu@ebonex.io").build();

        Spider spider = Spider.of().use(handler);
        Object o = spider.post("http://127.0.0.1:6050/message/sendMail", params);
        Assert.assertNotNull(o);
    }
}
