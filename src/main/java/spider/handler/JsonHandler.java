package spider.handler;

import com.google.gson.*;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import spider.base.Context;
import spider.utils.JsonUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Description: 解析json to object
 *
 * @author fengge.hu  @Date 2022/9/14
 **/
public class JsonHandler implements Handler {
    private final Gson gson = new GsonBuilder().create();
    /**
     * 提取json结构下的某个节点，再序列化为对象
     * eg: result.data.rows[1].kline
     */
    @Setter
    private String node;
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

    public JsonHandler(String node) {
        this.node = node;
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
        // 如果没有指定node或class，返回json对象
        if (StringUtils.isBlank(node) || null == clazz) {
            return element;
        }
        // 先解析node
        if (StringUtils.isNotBlank(node)) {
            element = JsonUtil.getJsonElement(element, node);
        }
        // 序列化class
        if (null != clazz) {
            if (element.isJsonArray()) {
                List ret = new LinkedList<>();
                for (JsonElement e : element.getAsJsonArray()) {
                    ret.add(gson.fromJson(e, clazz));
                }
                context.setResult(ret);
            } else {
                context.setResult(gson.fromJson(element, clazz));
            }
        } else {
            // 没有设置class就返回jsonElement
            context.setResult(element);
        }

        return context.getResult();
    }
}
