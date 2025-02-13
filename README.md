# low-spider
轻量级的爬虫： 保持简单易用的spider，定制Handler实现提取和处理信息

# handler
整个过程是处理器链。
- WebClientHandler: http request and get response
- parse response：JacksonHandler、JacksonDynamicHandler、GsonHandler、HtmlBodyHandler、HttpTableHandler
- CombinedHandler：把多个Handler组合在一起执行
- AsyncHandler：使1或多个Handler异步执行

# spring
可以定制多个spider实例，注册成spring bean

# demo
## create a spider for getting data
```java

@Bean
public Spider eastmoneyDatacenterSpider() {
    JacksonDynamicHandler jsonHandler = new JacksonDynamicHandler();
    jsonHandler.setNode("result");
    return Spider.of().use(webclientHandler, jsonHandler);
}

@Bean
public Spider eastmoneyFenshiSpider() {
    JacksonHandler jsonHandler = new JacksonHandler("data", TickData.class);
    return Spider.of().use(webclientHandler, jsonHandler);
}

// api -> kline
@Bean
public Spider eastmoneyKlineSpider() {
    GsonHandler gsonHandler = new GsonHandler();
    gsonHandler.setType(new TypeToken<EastmoneyResponse<EastmoneyData>>() {
    }.getType());
    return Spider.of().use(webclientHandler, gsonHandler);
}

@Bean
public Spider csindexRestSpider() {
    Map<String, String> header = new HashMap<String, String>() {{
        put("Accept", "application/json");
        put("Content-Type", "application/json;charset=UTF-8");
    }};
    HttpClientHandler handler = new HttpClientHandler(header);

    GsonHandler gsonHandler = new GsonHandler();
    gsonHandler.setType(CSIndexResponse.class);

    return Spider.of().use(handler, gsonHandler);
}

//
public Spider sinaRestSpider() {
        GsonHandler gsonHandler = new GsonHandler() {
            @Override
            protected void preRun(Context context) {
                String body = context.getBody();
                int start = body.indexOf("={");
                int end = body.indexOf("/*", start);
                if (start > 0) {
                    context.setBody(body.substring(start + 1, end));
                }
            }
    };
    gsonHandler.setClazz(SinaResponse.class);
    return Spider.of().use(webclientHandler, gsonHandler);
}
// 
public Spider baiduQuoteSpider(HttpClientHandler baiduHttpClient) {
    GsonHandler gsonHandler = new GsonHandler(OpendataMinuteData.class);
    gsonHandler.setNode("Result[1].DisplayData.resultData.tplData.result.minute_data");
    return Spider.of().use(baiduHttpClient, gsonHandler);
}

public Spider baiduMarketDataSpider(HttpClientHandler baiduHttpClient) {
    GsonHandler gsonHandler = new GsonHandler("Result.newMarketData");
    return Spider.of().use(baiduHttpClient, gsonHandler);
}
// 使用ExtSpider
public ExtSpider eastmoneyDatacenterSpider() {
    JacksonDynamicHandler jsonHandler = new JacksonDynamicHandler();
    jsonHandler.setNode("result");
    Spider spider = Spider.of().use(webclientHandler, jsonHandler);
    return ExtSpider.of(spider);
}
```
# mvn build
mvn clean deploy --settings ~/.m2/settings-ossrh.xml