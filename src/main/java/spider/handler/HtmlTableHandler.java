package spider.handler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import spider.base.Context;
import spider.model.ExcelValue;

import java.util.LinkedList;
import java.util.List;

/**
 * 解析标准的table/Excel格式
 *
 * @author fengge.hu  @Date 2022/9/15
 **/
@Slf4j
public class HtmlTableHandler implements Handler {
    // 通常表头只有1行，路径指定到cell - "#oMainTable > thead > tr > th"
    private String theadSelector;
    // 路径到行 - "#oMainTable > tbody > tr"
    private String tbodyTrSelector;

    public HtmlTableHandler(String thead, String tbodyTr) {
        this.theadSelector = thead;
        this.tbodyTrSelector = tbodyTr;
    }

    @Override
    public Object run(Context context) {
        Document doc = Jsoup.parse(context.getBody());
        String[] header = null;
        if (null != this.theadSelector) {   // 有些header不标准，不处理
            Elements head = doc.select(theadSelector); // 选择到 thead->tr->th
            if (null == head || head.isEmpty()) return null;
            header = new String[head.size()];
            for (int i = 0; i < head.size(); i++) { // th
                header[i] = head.get(i).text();
            }
        }

        Elements main = doc.select(tbodyTrSelector); // 选择到 tbody->tr
        List<String[]> ret = new LinkedList<>();
        if (null != main) {
            for (Element e : main) {  // 遍历tr
                Elements tds = e.getElementsByTag("td");
                String[] values = new String[tds.size()];
                for (int i = 0; i < tds.size(); i++) {
                    values[i] = tds.get(i).text();
                }
                ret.add(values);
            }
        }
        //
        context.result = ExcelValue.builder().header(header).values(ret).build();
        return context.getResult();
    }

}
