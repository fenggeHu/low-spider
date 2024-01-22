package spider.handler;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SilentJavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import spider.base.Context;
import spider.base.HttpMethod;
import spider.utils.JsonUtil;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Description:
 *
 * @author fengge.hu  @Date 2022/9/14
 **/
@Slf4j
public class WebClientHandler implements Handler {
    // 错误重试次数
    @Setter
    private int retry = 0;
    // 错误后休眠毫秒sleep
    @Setter
    private long sleep = 0;
    // proxy
    @Setter
    private spider.base.ProxyConfig proxyConfig;
    //
    @Setter
    private WebClient webClient;
    // 过期时间 ms - 默认15分钟
    @Setter
    private long expireTime = 15 * 60 * 1000;
    @Setter
    private String home;

    public WebClientHandler() {
    }

    public WebClientHandler(String home) {
        this.home = home;
    }

    @Override
    public void init() {
        if (null != webClient) {
            // 已经set了webclient就忽略
            return;
        }
        // http proxy
        if (null != proxyConfig) {
            webClient = new WebClient(BrowserVersion.CHROME, proxyConfig.getHost(), proxyConfig.getPort()); //创建一个webclient
            webClient.getOptions().getProxyConfig().setSocksProxy(proxyConfig.isSocks());
            if (null != proxyConfig.getUsername()) { // proxy username/password
                DefaultCredentialsProvider provider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
                provider.addCredentials(proxyConfig.getUsername(), proxyConfig.getPassword());
            }
        } else {
            webClient = new WebClient(BrowserVersion.CHROME); //创建一个webclient
        }
        webClient.getOptions().setJavaScriptEnabled(true); // 启动JS
        webClient.getOptions().setUseInsecureSSL(true);//忽略ssl认证 ******
        webClient.getOptions().setCssEnabled(false);//禁用Css，可避免自动二次请求CSS进行渲染
        webClient.getOptions().setThrowExceptionOnScriptError(false);//运行错误时，不抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());// 设置Ajax异步
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setTimeout(300000);//设置“浏览器”的请求超时时间
        webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener()); // 不打js异常
        webClient.setJavaScriptTimeout(5000);  // js timeout
        webClient.setIncorrectnessListener((message, origin) -> {
        }); // 忽略日志
    }

    private final AtomicLong lastTime = new AtomicLong(0);

    public WebClient getWebClient() {
        long now = System.currentTimeMillis();
        if (now - expireTime > lastTime.getAndSet(now)) { //闲置n分钟以上重新请求一下主页
            this.testHome();
        }
        return this.webClient;
    }

    private void testHome() {
        if (null != home && home.trim().length() > 0) {
            try {
                webClient.getPage(home);
                log.info("test ok: {}", home);
            } catch (Exception e) {
                log.error(home, e);
            }
        }
    }

    @SneakyThrows
    @Override
    public Object run(Context context) {
        int max = retry + 1;
        boolean executable = true;
        while (executable && max-- > 0) {
            try {
                Page result = getWebClient().getPage(this.webRequest(context));
                if (result instanceof HtmlPage) {
                    context.body = ((HtmlPage) result).asXml();
                } else if (result instanceof XmlPage) {
                    context.body = ((XmlPage) result).asXml();
                } else {
                    context.body = result.getWebResponse().getContentAsString();
                }
                context.setCode(result.getWebResponse().getStatusCode());
                List<NameValuePair> list = result.getWebResponse().getResponseHeaders();
                if (null != list && !list.isEmpty()) {
                    list.forEach(e -> context.setResponseHeader(e.getName(), e.getValue()));
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
    public WebRequest webRequest(final Context context) {
        WebRequest request;
        if (null != context.getParams()) {
            if (HttpMethod.GET.equals(context.getMethod())) {
                StringBuilder sb = new StringBuilder();
                Map<String, Object> params = JsonUtil.obj2map(context.getParams());
                for (Map.Entry<String, Object> kv : params.entrySet()) {
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

                request = new WebRequest(new URL(url), com.gargoylesoftware.htmlunit.HttpMethod.GET);
            } else { // post
                request = new WebRequest(new URL(context.getUrl()), com.gargoylesoftware.htmlunit.HttpMethod.POST);
                Map<String, Object> params = JsonUtil.obj2map(context.getParams());
                List<NameValuePair> parameters = new LinkedList<>();
                for (Map.Entry<String, Object> kv : params.entrySet()) {
                    parameters.add(new NameValuePair(kv.getKey(), String.valueOf(kv.getValue())));
                }
                request.setRequestParameters(parameters);
            }
        } else {
            request = new WebRequest(new URL(context.getUrl()), context.getMethod().equals(HttpMethod.GET) ?
                    com.gargoylesoftware.htmlunit.HttpMethod.GET : com.gargoylesoftware.htmlunit.HttpMethod.POST);
        }
        return request;
    }

}
