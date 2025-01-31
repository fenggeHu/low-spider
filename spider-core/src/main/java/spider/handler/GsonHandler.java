package spider.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import spider.base.Context;
import spider.utils.GsonUtil;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * Description: 解析json to object
 *
 * @author fengge.hu  @Date 2022/9/14
 **/
public class GsonHandler implements Handler {
    private final Gson gson = new GsonBuilder().create();
    /**
     * 提取json结构下的某个节点，再序列化为对象
     * eg: result.data.rows[1].kline
     */
    @Setter
    protected String node;
    @Setter
    protected Type type;

    public GsonHandler() {
    }

    public GsonHandler(String node) {
        this.node = node;
    }

    public GsonHandler(Type clazz) {
        this.type = clazz;
    }

    // 预处理 - 包含一些不规范的json字符串
    protected void preRun(final Context context) {
    }

    @Override
    public Object run(Context context) {
        if (StringUtils.isBlank(context.getBody())) return null;
        this.init();    // init

        this.preRun(context);

        JsonElement element = JsonParser.parseString(context.getBody());
        // 如果没有指定node或class，返回json对象
        if (StringUtils.isBlank(node) && null == type) {
            context.setResult(element);
            return element;
        }
        // 先解析node
        if (StringUtils.isNotBlank(node)) {
            element = GsonUtil.getJsonElement(element, node);
        }
        // 序列化class
        if (null != type) {
            context.setResult(this.parse(element, type));
        } else {
            // 没有设置class就返回jsonElement
            context.setResult(element);
        }

        return context.getResult();
    }

    // json element to Object
    protected Object parse(final JsonElement element, final Type difType) {
        if (null == element) return null;
        if (element.isJsonArray()) {
            List ret = new LinkedList<>();
            for (JsonElement e : element.getAsJsonArray()) {
                ret.add(gson.fromJson(e, difType));
            }
            return ret;
        } else {
            return gson.fromJson(element, difType);
        }
    }
}
