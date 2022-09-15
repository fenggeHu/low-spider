package spider.handler;

import spider.Context;

/**
 * @Description: data handler: input --> output
 * @Author jinfeng.hu  @Date 2022/9/8
 **/
public interface Handler {
    //
    default void init() {
    }

    void run(final Context context);
}
