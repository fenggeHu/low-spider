# low-spider
保持简单易用的spider，定制Handler实现提取和处理信息

# handler
所有的过程都是一连串的处理程序。
并发？-大部分情况不需要！个人轻量级使用的情况下，很多时候并发大量请求会导致账号或ip被封或服务器限流。
- WebClientHandler: http request and get response
- parse response：JsonHandler、HtmlBodyHandler、HttpTableHandler
- CombinedHandler：把多个Handler组合在一起执行
- AsyncHandler：使1或多个Handler异步执行

# spring
可以定制多个spider实例，注册成spring bean

# mvn build
mvn clean deploy --settings ~/.m2/settings-ossrh.xml

# demo
SpiderTests.java包含一些使用实例
## create a spider for getting data
```java
// create an eastmoney Spider
public Spider eastmoneyRestSpider() {
    JsonHandler jsonHandler = new JsonHandler();
    jsonHandler.setClazz(EastmoneyResponse.class);
    return Spider.of().use(webclientHandler, jsonHandler);
}
// 
public Spider csindexRestSpider() {
    Map<String, String> header = new HashMap<String, String>() {{
        put("Accept", "application/json");
        put("Content-Type", "application/json;charset=UTF-8");
    }};
    HttpClientHandler handler = new HttpClientHandler(header);

    JsonHandler jsonHandler = new JsonHandler();
    jsonHandler.setClazz(CSIndexResponse.class);

    return Spider.of().use(handler, jsonHandler);
}
//
public Spider sinaRestSpider() {
        JsonHandler jsonHandler = new JsonHandler() {
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
    jsonHandler.setClazz(SinaResponse.class);
    return Spider.of().use(webclientHandler, jsonHandler);
}
```
