package spider.crawler.bean;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.springframework.stereotype.Service;

/**
 * @author max.hu  @date 2024/12/24
 **/
@Service
public class PlaywrightBean {
    private String url = "https://www.jin10.com";

    public void run() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

//            page.onWebSocket(ws -> {
//                System.out.println("WebSocket 连接: " + ws.url());
//                ws.onFrameSent(frame -> System.out.println("WebSocket 发送: " + frame.text()));
//                ws.onFrameReceived(frame -> System.out.println("WebSocket 接收: " + frame.text()));
//                ws.onClose(code -> System.out.println("WebSocket 关闭: " + code));
//            });

            page.onRequest(request -> System.out.println("请求: " + request.method() + " " + request.url()));
            page.onResponse(response -> System.out.println("响应: " + response.status() + " " + response.url()));
//            page.onDOMContentLoaded(dom -> System.out.println("DOM: " + dom.content()));

            page.navigate(url);
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);

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
                    "           const xhr = new XMLHttpRequest();" +
                    "           xhr.open('POST', 'http://localhost:8080/callback/post', true);" +
                    "           xhr.setRequestHeader('Content-Type', 'application/json');" +
                    "           const data = {" +
                    "               'url': '" + url + "'," +
                    "               'name': mutation.attributeName," +
                    "               'target': mutation.target," +
                    "               'type': mutation.type" +
                    "           };" +
                    "           xhr.send(JSON.stringify(data));" +
                    "       }else if(mutation.type === 'characterData'){" +
                    "           console.log('文本内容变化：',mutation.target);" +
                    "       }" +
                    "    }" +
                    "});" +
                    "observer.observe(targetNode, config);");

//            page.waitForConsoleMessage(() -> System.out.println("有控制台信息！！！"));
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
}
