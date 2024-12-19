package spider;

import com.microsoft.playwright.*;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * https://github.com/microsoft/playwright-java?tab=readme-ov-file#is-playwright-thread-safe
 * @author max.hu  @date 2024/12/19
 **/
public class PlaywrightTests {
    @Test
    public void testDynamicWeb() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            // 创建一个浏览器上下文，类似于独立的浏览会话
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.navigate("https://www.jin10.com/");
            page.onResponse(response -> {
                System.out.println("onResponse===>");
                System.out.println(response.url());
                System.out.println(response.status());
                System.out.println(response.headers());
            });
            page.onWebSocket(webSocket -> {
                System.out.println("onWebSocket===>");
                System.out.println(webSocket.url());
                webSocket.onFrameReceived(message -> {
                    System.out.println("onFrameReceived===>");
                    System.out.println(message.text());
                });
                webSocket.onFrameSent(message -> {
                    System.out.println("onFrameSent===>");
                    System.out.println(message.text());
                });
            });

            // 使用循环或者定时器等方式保持浏览器活跃
            while (true) {
                // 这里可以执行定期检查或其他操作，避免程序退出
                Thread.sleep(10000);  // 每10秒执行一次（可以根据需要调整时间间隔）
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testScreenshot() {
        try (Playwright playwright = Playwright.create()) {
            List<BrowserType> browserTypes = Arrays.asList(
                    playwright.chromium(),
                    playwright.firefox()
            );
            for (BrowserType browserType : browserTypes) {
                try (Browser browser = browserType.launch()) {
                    BrowserContext context = browser.newContext();
                    Page page = context.newPage();
                    page.navigate("https://www.jin10.com/");
                    page.screenshot(new Page.ScreenshotOptions().setFullPage(true)
                            .setPath(Paths.get("screenshot-" + browserType.name() + ".png")));
                }
            }
        }
    }
}
