package spider.handler;

import spider.base.Context;

/**
 * Description: data handler: input - output
 * @author fengge.hu  @Date 2022/9/8
 **/
public interface Handler {
    //
    default void init() {
    }

    Object run(final Context context);
}
