package spider;

import org.junit.Test;
import spider.utils.UrlPathUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author max.hu  @date 2024/01/22
 **/
public class UrlPathUtilTests {

    @Test
    public void testPathParse() {
        String uri = "https://api.usstock.cloud/v1/stock/${symbol}/chart/${range}?token=${token}";
        Map<String, Object> vars = new HashMap<String, Object>() {{
            put("symbol", "AAPL");
            put("range", "5m");
            put("token", "pk_123456");
        }};
        String url = UrlPathUtil.parse(uri, vars);
        System.out.println(url);
    }


    @Test
    public void testPathParse2() {
        String uri = "https://api.usstock.cloud/v1/stock/${symbol}/chart/${range}?token=${token}";

        String url = UrlPathUtil.parse(uri, "symbol", "AAPL", "token", "pk_123456", "range", "5m");
        System.out.println(url);
    }
}
