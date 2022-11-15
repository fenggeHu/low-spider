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
    private final Map<String, String> params;
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

    public Context(String url, Map<String, String> params, HttpMethod method) {
        this.url = url;
        this.params = params;
        this.method = method;
    }

    // get instance
    public static Context get(String uri, Map<String, String> params) {
        return Context.of(uri, params, HttpMethod.GET);
    }

    public static Context get(String uri) {
        return Context.of(uri, null, HttpMethod.GET);
    }

    public static Context post(String uri, Map<String, String> params) {
        return Context.of(uri, params, HttpMethod.POST);
    }

    public static Context of(String uri, Map<String, String> params, HttpMethod method) {
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
}
