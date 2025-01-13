package spider.base;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 异步交互的Context
 *
 * @author max.hu  @date 2024/12/25
 **/
public class AsyncContext<T> extends Context {
    // 异步接收到的数据
    private final Queue<T> queue;

    public AsyncContext(String url, Object params, HttpMethod method, Queue<T> queue) {
        super(url, params, method);
        this.queue = queue;
    }

    public AsyncContext(String url, Object params, HttpMethod method, int capacity) {
        super(url, params, method);
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    public AsyncContext(String url, Object params, HttpMethod method) {
        super(url, params, method);
        this.queue = new LinkedBlockingQueue<>();
    }
}
