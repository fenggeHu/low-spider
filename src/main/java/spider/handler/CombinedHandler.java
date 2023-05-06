package spider.handler;

import lombok.extern.slf4j.Slf4j;
import spider.base.Context;

import java.util.LinkedList;
import java.util.List;

/**
 * 组合/复合Handler
 *
 * @author jinfeng.hu  @date 2023/5/6
 **/
@Slf4j
public class CombinedHandler implements Handler {
    // handlers
    private final List<Handler> handlers = new LinkedList<>();

    public static CombinedHandler of(Handler... moreHandlers) {
        return new CombinedHandler(moreHandlers);
    }

    // 构造Handler
    public CombinedHandler(Handler... moreHandlers) {
        for (Handler h : moreHandlers) {
            this.handlers.add(h);
        }
    }

    @Override
    public void init() {
        for (Handler h : handlers) {
            h.init(); // init
        }
    }

    @Override
    public Object run(Context context) {
        Object result = null;
        for (Handler h : handlers) {
            result = h.run(context); // run
            if (log.isDebugEnabled()) {
                log.debug("run: {}", h.getClass().getSimpleName());
            }
        }
        return result;
    }
}
