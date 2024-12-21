package spider;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import spider.base.Context;
import spider.handler.Handler;
import spider.handler.HttpClientHandler;

import java.util.function.Function;

/**
 * 扩展spider功能 - for test
 *
 * @author max.hu  @date 2024/12/21
 **/
@Slf4j
public class SpiderEx {
    @Getter
    private final Context context;

    private SpiderEx(Context context) {
        this.context = context;
    }

    public Object get() {
        return this.getContext().get();
    }

    public SpiderEx run(Function<Context, Object> func) {
        func.apply(this.getContext());
        return this;
    }

    public <T extends Handler> SpiderEx run(T handler) {
        handler.run(this.getContext());
        return this;
    }

    public SpiderEx run(Class<? extends Handler> handlerClass) {
        try {
            Handler handler = handlerClass.newInstance();
            handler.run(this.getContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    // get instance
    public static SpiderEx get(String uri) {
        return new SpiderEx(Context.get(uri));
    }

    public static SpiderEx request(final Context context) {
        return new SpiderEx(context);
    }

    public static void main(String[] args) {
        Object obj = SpiderEx.get("https://www.baidu.com").run(HttpClientHandler.class)
                .get();
        System.out.println(obj);
    }

}
