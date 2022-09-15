package spider;

import lombok.Data;

import java.util.Map;

/**
 * @Description: record context
 * @Author Jinfeng.hu  @Date 2022/9/15
 **/
@Data
public class Context {
    private final String url;
    private final Map<String, String> params;
    private final HttpMethod method;
    // cookie todo

    // http response body
    public String body;
    // http status code
    public int code;
    // err msg
    public String msg;
    // 处理的结果
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
}
