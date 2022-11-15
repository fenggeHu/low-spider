package spider.handler;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import spider.base.Context;
import spider.base.HttpMethod;

import java.util.Map;

/**
 * Description: http client - use OkHttpClient
 *
 * @author fengge.hu  @Date 2022/11/15
 **/
@Slf4j
public class HttpClientHandler implements Handler {
    // 错误重试次数
    @Setter
    private int retry = 0;
    // 错误后休眠毫秒sleep
    @Setter
    private long sleep = 0;
    // proxy
//    @Setter
//    private spider.base.ProxyConfig proxyConfig;
    //
    @Setter
    private OkHttpClient httpClient;
    @Setter
    private Map<String, String> header;

    public HttpClientHandler() {
    }

    public HttpClientHandler(Map<String, String> header) {
        this.header = header;
    }

    @Override
    public void init() {
        if (null != httpClient) {
            // 已经set了就忽略
            return;
        }
        this.httpClient = new OkHttpClient();
        // http proxy todo
    }

    @SneakyThrows
    @Override
    public Object run(Context context) {
        int max = retry + 1;
        boolean executable = true;
        while (executable && max-- > 0) {
            try (Response response = httpClient.newCall(this.webRequest(context)).execute()) {
                context.setCode(response.code());
                if (null != response.headers()) {
                    response.headers().forEach(e -> context.setResponseHeader(e.getFirst(), e.getSecond()));
                }
                context.body = response.body().string();
                if (!response.isSuccessful()) {
                    context.msg = response.message();
                }
                executable = false;
            } catch (Exception e) {
                log.error("getPage: " + context.getUrl(), e);
                if (max > 0 && sleep > 0) {
                    Thread.sleep(sleep);
                }
            }
        }
        return context.getBody();
    }

    @SneakyThrows
    public Request webRequest(final Context context) {
        Request.Builder builder = new Request.Builder();
        // 带参数
        if (null != context.getParams() && context.getParams().size() > 0) {
            if (HttpMethod.GET.equals(context.getMethod())) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> kv : context.getParams().entrySet()) {
                    if (sb.length() > 0) sb.append("&");
                    sb.append(kv.getKey()).append("=").append(kv.getValue());
                }
                String url = context.getUrl();
                int index = context.getUrl().lastIndexOf("/");
                if (context.getUrl().indexOf("?", index) > 0) {
                    url += "&" + sb;
                } else {
                    url += "?" + sb;
                }

                builder.get().url(url);
            } else { // post
                FormBody.Builder fb = new FormBody.Builder();
                for (Map.Entry<String, String> kv : context.getParams().entrySet()) {
                    fb.add(kv.getKey(), kv.getValue());
                }
                builder.url(context.getUrl()).post(fb.build());
            }
        } else {    // 不带参数
            if (HttpMethod.GET.equals(context.getMethod())) {
                builder.get().url(context.getUrl());
            } else {
                builder.url(context.getUrl());
            }
        }

        if (null != this.header) {
            this.header.forEach((k, v) -> builder.addHeader(k, v));
        }

        return builder.build();
    }
}
