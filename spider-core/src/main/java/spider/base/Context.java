package spider.base;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: record context
 *
 * @author Jinfeng.hu  @Date 2022/9/15
 **/
public class Context {
    @Getter
    private final String url;
    @Getter
    private final Object params;
    @Getter
    private final HttpMethod method;
    // cookie todo
    // response header
    @Getter
    private Map<String, String> responseHeader;
    // http response body
    @Getter
    @Setter
    public String body;
    // http status code
    @Getter
    @Setter
    public int code;
    // err msg
    @Getter
    @Setter
    public String msg;
    // 处理的结果
    @Getter
    @Setter
    public Object result;
    // 使用时传递的的自定义变量<K，V>
    private Map<String, Object> customVariables;

    public Context(String url, Object params, HttpMethod method) {
        this.url = url;
        this.params = params;
        this.method = method;
    }

    public Object get() {
        return this.result == null ? this.getBody() : this.getResult();
    }

    // get instance
    public static Context get(String uri, Map<String, String> params) {
        return Context.of(uri, params, HttpMethod.GET);
    }

    public static Context get(String uri) {
        return Context.of(uri, null, HttpMethod.GET);
    }

    public static Context post(String uri, Object params) {
        return Context.of(uri, params, HttpMethod.POST);
    }

    public static Context of(String uri, Object params, HttpMethod method) {
        Context context = new Context(uri, params, method);
        return context;
    }

    //
    public Context setResponseHeader(String name, String value) {
        if (null == this.responseHeader) {
            this.responseHeader = new HashMap<>();
        }
        this.responseHeader.put(name, value);
        return this;
    }

    public Context addCustomVar(String name, Object value) {
        if (null == this.customVariables) {
            this.customVariables = new HashMap();
        }
        this.customVariables.put(name, value);
        return this;
    }

    public Object getCustomVar(String name) {
        if (null == this.customVariables) {
            return null;
        }
        return this.customVariables.get(name);
    }
}
