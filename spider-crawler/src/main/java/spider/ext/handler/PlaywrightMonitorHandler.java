package spider.ext.handler;

import com.microsoft.playwright.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author max.hu  @date 2024/12/24
 **/
@Slf4j
public class PlaywrightMonitorHandler {
    // target page url
    private String targetUrl;
    // observer
    private String targetSelector; //  "#clazz" ".some-div"
    // callback url
    private String callback;

    public static PlaywrightMonitorHandler of(String url, String targetSelector, String callback) {
        PlaywrightMonitorHandler pwb = new PlaywrightMonitorHandler();
        pwb.targetUrl = url;
        pwb.targetSelector = targetSelector;
        pwb.callback = callback;
        return pwb;
    }

    private boolean isClose = true;

    public void run() {
        while (true) {  // 只要playwright启动失败，就一直循环重启
            if (isClose) {
                this.start();
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error("Thread.sleep异常", e);
            }
        }
    }

    private void start() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.navigate(targetUrl);
            // JavaScript: MutationObserver
            String mutationObserver = "const targetNode = document.querySelector('" + targetSelector + "');" +
                    "const config = { attributes: true, childList: true, subtree: true, characterData:true };" +
                    "const observer = new MutationObserver(mutationsList => {" +
                    "    for (const mutation of mutationsList) {" +
                    "       if (mutation.type === 'attributes') {" +
                    "           const form = new FormData();" +
                    "           form.append('url', '" + targetUrl + "');" +
                    "           form.append('name', mutation.attributeName);" +
                    "           form.append('html', mutation.target.outerHTML);" +
                    "           fetch('" + callback + "', {method: 'POST', body: form});" +
                    "       }" +
                    "    }" +
                    "});" +
                    "observer.observe(targetNode, config);";
            log.info(mutationObserver);
            page.evaluate(mutationObserver);

            isClose = false;
            log.info("{} playwright启动成功", targetUrl);
            // 持续监听 (使用循环，但请注意资源消耗，并根据需要添加退出机制)
            while (true) {
                try {
                    Thread.sleep(5000); // 每 5 秒检查一次
                } catch (InterruptedException e) {
                    log.error("Thread.sleep异常", e);
                }
            }
        } catch (Exception e) {
            log.error("playwright异常", e);
            isClose = true;
        }
    }
}
