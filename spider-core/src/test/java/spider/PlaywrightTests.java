package spider;

import com.microsoft.playwright.*;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * https://github.com/microsoft/playwright-java?tab=readme-ov-file#is-playwright-thread-safe
 *
 * @author max.hu  @date 2024/12/19
 **/
public class PlaywrightTests {

    @Test
    public void testFromGemini() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.onWebSocket(ws -> {
                System.out.println("WebSocket 连接: " + ws.url());
                ws.onFrameSent(frame -> System.out.println("WebSocket 发送: " + frame.text()));
                ws.onFrameReceived(frame -> System.out.println("WebSocket 接收: " + frame.text()));
                ws.onClose(code -> System.out.println("WebSocket 关闭: " + code));
            });

            page.onRequest(request -> System.out.println("请求: " + request.method() + " " + request.url()));
            page.onResponse(response -> System.out.println("响应: " + response.status() + " " + response.url()));
            page.onDOMContentLoaded(dom -> System.out.println("DOM: " + dom.content()));

            page.navigate("https://www.jin10.com");

            // 使用 page.waitForFunction 监听页面状态变化 (例如，某个特定的文本出现)
//            page.waitForFunction("() => document.body.innerText.includes('关键文本')", null, new Page.WaitForFunctionOptions().setTimeout(0));
            // 监听特定区域的 DOM 变化 (例如，整个 body 或某个特定的 div)
            String targetSelector = "#jin_flash_list"; // 或 ".some-div" 等更精确的选择器
            // 在浏览器上下文中执行 JavaScript 代码，创建 MutationObserver
            page.evaluate("const targetNode = document.querySelector('" + targetSelector + "');" +
                    "const config = { attributes: true, childList: true, subtree: true, characterData:true };" + // 监听属性、子节点和子树的变化
                    "const observer = new MutationObserver(mutationsList => {" +
                    "    for (const mutation of mutationsList) {" +
                    "       if (mutation.type === 'childList') {" +
                    "            console.log('子节点变化：', mutation.addedNodes,mutation.removedNodes);" +
                    "       } else if (mutation.type === 'attributes') {" +
                    "            console.log('属性变化：', mutation.attributeName, mutation.target);" +
                    "       }else if(mutation.type === 'characterData'){" +
                    "           console.log('文本内容变化：',mutation.target);" +
                    "       }" +
                    "    }" +
                    "});" +
                    "observer.observe(targetNode, config);");

            page.waitForConsoleMessage(() -> System.out.println("有控制台信息！！！"));
//            page.onConsoleMessage(msg -> System.out.println("控制台信息: " + msg.text()));
            page.onConsoleMessage(msg -> {
                System.out.println("控制台信息: " + msg.text());
                if (msg.type().equals("log")) {
                    System.out.println("DOM 变化: " + msg.args().toString());
                }
            });


            // 持续监听 (使用循环，但请注意资源消耗，并根据需要添加退出机制)
            while (true) {
                try {
                    Thread.sleep(5000); // 每 5 秒检查一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break; // 允许中断循环
                }
            }

            // 关闭浏览器
            browser.close();
        }
    }

    @Test
    public void testDynamicWeb() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            // 创建一个浏览器上下文，类似于独立的浏览会话
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.onRequest(request -> System.out.println(request));
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
                webSocket.onClose(ws -> {
                    System.out.println("onClose===>" + ws.isClosed());
                    System.out.println(ws.url());
                });
            });

            page.navigate("https://www.jin10.com");
//            // 使用循环或者定时器等方式保持浏览器活跃
//            while (true) {
//                // 这里可以执行定期检查或其他操作，避免程序退出
//                Thread.sleep(10000);  // 每10秒执行一次（可以根据需要调整时间间隔）
//            }
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
                    page.navigate("https://www.google.com/");
                    page.screenshot(new Page.ScreenshotOptions().setFullPage(true)
                            .setPath(Paths.get("screenshot-" + browserType.name() + ".png")));
                }
            }
        }
    }
}
