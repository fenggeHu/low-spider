package spider.crawler.bean;

import com.microsoft.playwright.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author max.hu  @date 2024/12/24
 **/
@Slf4j
public class PlaywrightHandler {
    // page url
    private String url;
    // callback url
    private String callback;
    // observer
    private String targetSelector = "#jin_flash_list"; //  ".some-div"

    public static PlaywrightHandler of(String url, String targetSelector, String callback) {
        PlaywrightHandler pwb = new PlaywrightHandler();
        pwb.url = url;
        pwb.targetSelector = targetSelector;
        pwb.callback = callback;
        return pwb;
    }

    private boolean isClose = true;

    private int count = 5;

    public void run() {
        while (count-- > 0) {
            if (isClose) {
                this.playwright();
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error("Thread.sleep异常", e);
            }
        }
    }

    private void playwright() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.navigate(url);
            // JavaScript: MutationObserver
            String mutationObserver = "const targetNode = document.querySelector('" + targetSelector + "');" +
                    "const config = { attributes: true, childList: true, subtree: true, characterData:true };" +
                    "const observer = new MutationObserver(mutationsList => {" +
                    "    for (const mutation of mutationsList) {" +
                    "       if (mutation.type === 'attributes') {" +
                    "           const form = new FormData();" +
                    "           form.append('url', '" + url + "');" +
                    "           form.append('name', mutation.attributeName);" +
                    "           form.append('target', mutation.target.outerHTML);" +
                    "           fetch('" + callback + "', {method: 'POST', body: form});" +
                    "       }" +
                    "    }" +
                    "});" +
                    "observer.observe(targetNode, config);";
            log.info(mutationObserver);

            page.evaluate(mutationObserver);

            isClose = false;
            log.info("{} playwright启动成功", url);
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
