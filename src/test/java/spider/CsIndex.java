package spider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import spider.handler.HttpClientHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jinfeng.hu  @Date 2022/11/23
 **/
public class CsIndex {

    @Before
    public void before() {

    }

    @Test
    public void getIndustry() {
        Map<String, String> header = new HashMap<String, String>() {{
            put("Accept", "application/json");
            put("Content-Type", "application/json;charset=UTF-8");
        }};
        HttpClientHandler handler = new HttpClientHandler(header);

        Map<String, Object> payload = new HashMap<>();
        payload.put("searchInput", "");
        payload.put("pageNum", 1);
        payload.put("pageSize", 40);
        payload.put("sortField", null);
        payload.put("sortOrder", null);

        Spider spider = Spider.of().use(handler);

        Object o = spider.post("https://www.csindex.com.cn/csindex-home/indexInfo/security-industry-search", payload);
        Assert.assertNotNull(o);
    }
}
