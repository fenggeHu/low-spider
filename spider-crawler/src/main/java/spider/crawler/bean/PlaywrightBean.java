package spider.crawler.bean;

import com.microsoft.playwright.*;
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
//                System.out.println("WebSocket Url: " + ws.url());
//                ws.onFrameSent(frame -> System.out.println("WebSocket Sent: " + frame.text()));
//                ws.onFrameReceived(frame -> System.out.println("WebSocket Received: " + frame.text()));
//                ws.onClose(code -> System.out.println("WebSocket Close: " + code));
//            });

            page.onRequest(request -> System.out.println("Request: " + request.method() + " " + request.url()));
            page.onResponse(response -> System.out.println("Response: " + response.status() + " " + response.url()));

            page.navigate(url);

            // observer
            String targetSelector = "#jin_flash_list"; //  ".some-div"
            // JavaScript: MutationObserver
            page.evaluate("const targetNode = document.querySelector('" + targetSelector + "');" +
                    "const config = { attributes: true, childList: true, subtree: true, characterData:true };" +
                    "const observer = new MutationObserver(mutationsList => {" +
                    "    for (const mutation of mutationsList) {" +
                    "       if (mutation.type === 'childList') {" +
                    "            console.log('nodes：', mutation.addedNodes,mutation.removedNodes);" +
                    "       } else if (mutation.type === 'attributes') {" +
                    "            console.log('attribute：', mutation.attributeName, mutation.target);" +
                    "           const form = new FormData();" +
                    "           form.append('url', '" + url + "');" +
                    "           form.append('name', mutation.attributeName);" +
                    "           form.append('target', mutation.target.outerHTML);" +
                    "           fetch('http://localhost:8080/callback/post', {method: 'POST', body: form});" +
                    "       }else if(mutation.type === 'characterData'){" +
                    "           console.log('target：',mutation.target);" +
                    "       }" +
                    "    }" +
                    "});" +
                    "observer.observe(targetNode, config);");

//            page.onConsoleMessage(msg -> System.out.println("Console: " + msg.text()));
            page.onConsoleMessage(msg -> {
                System.out.println("Console: " + msg.text());
                if (msg.type().equals("log")) {
                    System.out.println("DOM Changed: " + msg.args().toString());
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
