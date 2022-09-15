package spider;

import spider.handler.Handler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author jinfeng.hu  @Date 2022/9/14
 **/
public class Spider {
    // data handlers
    private List<Handler> handlers = new LinkedList<>();

    public Spider use(Handler... handlers) {
        for (Handler h : handlers) {
            h.init(); // init
            this.handlers.add(h);
        }
        return this;
    }

    //
    public Object request(String uri, Map<String, String> params, HttpMethod method) {
        Context context = Context.of(uri, params, method);
        for (Handler h : handlers) {
            h.run(context);
        }
        return context.getResult() == null ? context.getBody() : context.getResult();
    }

    //
    public Object get(String uri) {
        return get(uri, null);
    }

    public Object get(String uri, Map<String, String> params) {
        return request(uri, params, HttpMethod.GET);
    }

    //
    public Object post(String uri, Map<String, String> params) {
        return request(uri, params, HttpMethod.POST);
    }

    public static Spider of() {
        return new Spider();
    }
}
