package spider.handler;

import com.google.gson.*;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import spider.Context;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 解析json to object
 * @Author jinfeng.hu  @Date 2022/9/14
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

    @Override
    public void run(Context context) {
        if (StringUtils.isBlank(context.getBody())) return;
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
    }
}
