package spider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import spider.handler.ChromeHandler;
import spider.handler.Handler;
import spider.handler.JsonHandler;
import spider.handler.WebClientHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author jinfeng.hu  @Date 2022/9/14
 **/
@Slf4j
public class SpiderTests {
    private static Gson gson = new GsonBuilder().create();

    @Test
    public void testCustomHandler1() {
        Spider spider = Spider.of().use(new WebClientHandler()).use(new Handler() {
            @Override
            public void init() {
                // TODO
            }

            @Override
            public void run(Context context) {
                // todo
            }
        });
    }
    @Test
    public void testCustomHandler() {
        Spider spider = Spider.of().use(new WebClientHandler()).use(context -> {
            List<Rank> ret = new LinkedList<>();
            Document doc = Jsoup.parse(context.getBody());
            Elements head = doc.select("#oMainTable > thead > tr > th");// #oMainTable > thead > tr > th
            String[] header = new String[head.size()];
            for (int i = 0; i < head.size(); i++) { // th
                header[i] = head.get(i).text();
            }
            // #oMainTable > tbody > tr
            Elements main = doc.select("#oMainTable > tbody > tr");
            for (Element e : main) {  // tr
                Elements tds = e.getElementsByTag("td");
                if (tds.size() != head.size()) {
                    log.error("解析数据错误: {}", head.text());
                    continue;
                }
                Rank rank = Rank.builder().build();
                for (int i = 0; i < tds.size(); i++) {
                    String val = tds.get(i).text();
                    if ("名次".equals(header[i])) {
                        rank.setRank(Integer.valueOf(val));
                    } else if ("代碼".equals(header[i])) {
                        rank.setSymbol(val);
                    } else if ("名稱".equals(header[i])) {
                        rank.setName(val);
                    } else if ("日期".equals(header[i])) {
                        rank.setDatetime(val);
                    } else if ("價格".equals(header[i])) {
                        rank.setClose(Double.valueOf(val.replaceAll(",", "")));
                    } else if ("成交量".equals(header[i])) {
                        rank.setVolume(Long.valueOf(val.replaceAll(",", "")));
                    } else if (header[i].indexOf("成交值") >= 0) {
                        rank.setTurnover(new Double(Double.parseDouble(val.replaceAll(",", "")) * 1000).longValue());
                    }
                }
                ret.add(rank);
            }
            context.setResult(ret);
        });
        List<Rank> ret = (List<Rank>) spider.get("https://www.XXXX.com/rank0001");
        System.out.println(ret);
    }

    @Test
    public void testSpider() {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "SZ300349,SZ000818,SZ300034,SZ000963,SZ002371");
        params.put("_", "" + System.currentTimeMillis());

        Spider spider = Spider.of().use(new ChromeHandler());
        Object result = spider.get("https://stock.xxxxxyyyyyy.com/v5/stock/realtime/quotec.json?symbol=SH600436,SZ002612,SZ300896&_=");
        Assert.assertNotNull(result);

        JsonHandler jsonHandler = new JsonHandler();
        jsonHandler.setClazz(Index.class);
        Spider spider2 = Spider.of().use(new WebClientHandler()).use(jsonHandler);
        Object result2 = spider2.get("https://www.xxxx.com/us/rest/DJI");
        Assert.assertNotNull(result2);
    }

    @Test
    public void testJsonHandler() {
        JsonHandler jsonHandler = new JsonHandler();
        jsonHandler.setClazz(Rank.class);
        Rank rank = Rank.builder().rank(1).close(123.4).datetime("2022-09-14").build();
        Context context = Context.of("", null, null);
        context.setBody(gson.toJson(rank));
        jsonHandler.run(context);
        Assert.assertNotNull(context.getResult());
    }
}
