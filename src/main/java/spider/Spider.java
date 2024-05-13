package spider;

import lombok.extern.slf4j.Slf4j;
import spider.base.Context;
import spider.base.HttpMethod;
import spider.handler.Handler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author fengge.hu  @Date 2022/9/14
 **/
@Slf4j
public class Spider {
    // handlers
    private final List<Handler> handlers = new LinkedList<>();

    // 加入顺序即为执行顺序
    public Spider use(Handler... handlers) {
        for (Handler h : handlers) {
            h.init(); // init
            this.handlers.add(h);
        }
        return this;
    }

    // 如果想拿到更多response信息，使用这个方法 - 从context取更多信息
    public Object request(final Context context) {
        if (log.isDebugEnabled()) {
            log.debug("{} Request: {}", context.getMethod(), context.getUrl());
        }
        for (Handler h : handlers) {
            h.run(context);
        }
        return context.getResult();
    }

    //
    public Object request(String uri, Object params, HttpMethod method) {
        Context context = Context.of(uri, params, method);
        return request(context);
    }

    //
    public Object get(String uri) {
        return get(uri, null);
    }
    // get请求的参数和值都是String更符合实际情况 - Map<String, String>
    public Object get(String uri, Map<String, String> params) {
        return request(uri, params, HttpMethod.GET);
    }

    //
    public Object post(String uri, Object params) {
        return request(uri, params, HttpMethod.POST);
    }

    public static Spider of() {
        return new Spider();
    }
}
