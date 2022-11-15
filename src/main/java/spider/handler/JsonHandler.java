package spider.handler;

import com.google.gson.*;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import spider.base.Context;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Description: 解析json to object
 *
 * @author fengge.hu  @Date 2022/9/14
 **/
public class JsonHandler implements Handler {
    private final static Gson gson = new GsonBuilder().create();
    @Setter
    private Class clazz;

    @Override
    public void init() {
        if (null == clazz) {
            clazz = Map.class;
        }
    }

    public JsonHandler() {
    }

    public JsonHandler(Class clazz) {
        this.clazz = clazz;
    }

    // 预处理 - 包含一些不规范的json字符串
    protected void preRun(final Context context) {
    }

    @Override
    public Object run(Context context) {
        if (StringUtils.isBlank(context.getBody())) return null;
        this.preRun(context);

        JsonElement element = JsonParser.parseString(context.getBody());
        if (element.isJsonArray()) {
            List ret = new LinkedList<>();
            for (JsonElement e : element.getAsJsonArray()) {
                ret.add(gson.fromJson(e, clazz));
            }
            context.setResult(ret);
        } else {
            context.setResult(gson.fromJson(element, clazz));
        }

        return context.getResult();
    }
}
