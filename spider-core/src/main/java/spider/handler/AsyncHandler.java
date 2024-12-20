package spider.handler;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import spider.base.Context;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 异步执行的Handler
 *
 * @author jinfeng.hu  @date 2023/5/6
 **/
@Slf4j
public class AsyncHandler implements Handler {
    // 1..N handlers
    private final List<Handler> handlers = new LinkedList<>();
    @Setter
    private Executor executor;
    @Setter
    private int corePoolSize = 1;
    @Setter
    private int maxPoolSize = 5;
    @Setter
    private int keepAliveSeconds = 60;
    @Setter
    private int queueCapacity = 100;
    @Setter
    private ThreadFactory threadFactory;
    @Setter
    private RejectedExecutionHandler rejectedExecutionHandler;

    public static AsyncHandler of(Handler... handlers) {
        return new AsyncHandler(handlers);
    }

    // 构造Handler
    public AsyncHandler(Handler... handlers) {
        for (Handler h : handlers) {
            this.handlers.add(h);
        }
    }

    @Override
    public void init() {
        for (Handler h : handlers) {
            h.init(); // init
        }
        if (null == this.executor) {
            this.executor = this.buildExecutor();
        }
    }

    @Override
    public Object run(Context context) {
        for (Handler h : handlers) {
            CompletableFuture.runAsync(() -> h.run(context), executor); // async run
            if (log.isDebugEnabled()) {
                log.debug("runAsync: {}", h.getClass().getSimpleName());
            }
        }
        return true;
    }

    // 构建Executor
    private Executor buildExecutor() {
        BlockingQueue<Runnable> queue = queueCapacity > 0 ? new LinkedBlockingQueue<>(queueCapacity) : new SynchronousQueue<>();
        if (null == threadFactory) {
            threadFactory = Executors.defaultThreadFactory();
        }
        if (null == rejectedExecutionHandler) {
            rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
        }
        return new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize,
                this.keepAliveSeconds, TimeUnit.SECONDS,
                queue, threadFactory, rejectedExecutionHandler);
    }
}
