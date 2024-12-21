package spider.handler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import spider.base.Context;

/**
 * Description: 提取html body text -- context.body
 * @author Jinfeng.hu  @Date 2022-09-18
 **/
public class HtmlBodyHandler implements Handler {
    @Override
    public Object run(Context context) {
        this.init();    // init

        Document doc = Jsoup.parse(context.getBody());
        context.setBody(doc.body().text());
        return context.getBody();
    }
}
