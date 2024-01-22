package spider.utils;

import com.google.gson.*;

import java.util.HashMap;
import java.util.Map;

/**
 * json
 * @author Jinfeng.hu  @Date 2021-11-2021/11/22
 **/
public class JsonUtil {
    // 默认序列化忽略null
    private final static Gson gson = new GsonBuilder().create();

    public static <T> T map2Obj(Map<String, Object> map, Class<T> t) {
        return toObject(toJson(map), t);
    }

    public static Map obj2map(Object obj) {
        if (obj instanceof Map) {
            return (Map) obj;
        } else {
            String json = toJson(obj);
            return toObject(json, HashMap.class);
        }
    }

    public static <T> T toObject(String json, Class<T> t) {
        return gson.fromJson(json, t);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static JsonObject getJsonObject(String json) {
        return gson.fromJson(json, JsonElement.class).getAsJsonObject();
    }

}
