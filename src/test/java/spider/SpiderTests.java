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
import spider.base.Context;
import spider.handler.*;
import spider.model.ExcelValue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author fengge.hu  @Date 2022/9/14
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
            public Object run(Context context) {
                // todo
                return null;
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
            return ret;
        });
        List<Rank> ret = (List<Rank>) spider.get("https://www.XXXX.com/rank0001");
        System.out.println(ret);
    }

    @Test
    public void testHtmlTableHandler() {
        Spider spider = Spider.of().use(new WebClientHandler())
                .use(new HtmlTableHandler("#oMainTable > thead > tr > th", "#oMainTable > tbody > tr"));
        ExcelValue ret = (ExcelValue) spider.get("https://www.XXX.com/us/rank/rank0001");
        System.out.println(ret);
    }

    @Test
    public void testHtmlTableHandler2() {
        Spider spider = Spider.of().use(new WebClientHandler(), new HtmlTableHandler(null, "#NewStockTable > tbody > tr"));
        ExcelValue ret = (ExcelValue) spider.get("http://vip.stock.finance.sina.com.cn/corp/view/vII_NewestComponent.php?page=1&indexid=000300");
        System.out.println(ret);
    }

    @Test
    public void testHtmlTableHandler3() {
        Spider spider = Spider.of().use(new WebClientHandler(), new HtmlTableHandler("#cr1 > thead > tr > th", "#cr1 > tbody > tr"));
        ExcelValue ret = (ExcelValue) spider.get("https://cn.investing.com/indices/investing.com-us-500-components");
        System.out.println(ret);
        List<String> values = ret.get("名称");
        System.out.println(values);
    }

    @Test
    public void testHtmlTableHandler4() {
        Spider spider = Spider.of().use(new WebClientHandler(), new HtmlTableHandler("#constituents > tbody > tr > th", "#constituents > tbody > tr"));
        ExcelValue ret = (ExcelValue) spider.get("https://zh.wikipedia.org/wiki/S%26P_500%E6%88%90%E4%BB%BD%E8%82%A1%E5%88%97%E8%A1%A8");
        System.out.println(ret);
        List<String> values = ret.get(0);
        System.out.println(values);
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
