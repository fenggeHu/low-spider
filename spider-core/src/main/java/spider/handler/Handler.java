package spider.handler;

import spider.base.Context;

/**
 * Description: data handler: input - output
 * @author fengge.hu  @Date 2022/9/8
 **/
public interface Handler {
    // init properties of this handler
    default void init() {
    }
    // handler执行&返回
    Object run(final Context context);
}
