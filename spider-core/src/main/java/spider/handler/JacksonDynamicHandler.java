package spider.handler;

import com.fasterxml.jackson.databind.JsonNode;
import spider.base.Context;
import spider.utils.JacksonUtil;

import java.lang.reflect.Type;

/**
 * 动态解析json
 * 1，为了支持很多接口只是最后的response格式的细微差异
 *
 * @author max.hu  @date 2024/03/07
 **/
public class JacksonDynamicHandler extends JacksonHandler {
    public final static String CUSTOM_KEY = "Dynamic_Java_Type";

    @Override
    public Object run(final Context context) {
        Object obj = super.run(context);
        // 如果没有指定type，尝试使用自定义的类型
        if (null == this.type) {
            Type type = (Type) context.getCustomVar(CUSTOM_KEY);
            if (null != type) {
                obj = JacksonUtil.parse((JsonNode) obj, type);
                context.setResult(obj);
            }
        }
        return obj;
    }
}
