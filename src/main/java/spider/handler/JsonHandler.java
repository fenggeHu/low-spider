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
    private static Gson gson = new GsonBuilder().create();
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

    @Override
    public Object run(Context context) {
        if (StringUtils.isBlank(context.getBody())) return null;
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
