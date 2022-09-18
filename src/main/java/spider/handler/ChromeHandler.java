package spider.handler;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import spider.base.Context;

/**
 * Description: selenium chrome
 *
 * @author fengge.hu  @Date 2022/9/14
 **/
public class ChromeHandler implements Handler {
    private WebDriver driver;
    // chrome web driver binary file
    @Setter
    private String chrome;
    @Setter
    private String home;

    // 有时候需要预加载一个起始页面
    @Override
    public void init() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); //无浏览器模式
        options.addArguments("--no-sandbox"); // unknown error: DevToolsActivePort file doesn't exist
        options.addArguments("--disable-dev-shm-usage"); // unknown error: DevToolsActivePort file doesn't exist
        options.addArguments("--disable-gpu"); // unknown error: DevToolsActivePort file doesn't exist
        options.addArguments("--disable-extensions"); // unknown error: DevToolsActivePort file doesn't exist
        if (StringUtils.isNotBlank(chrome)) {
            options.setBinary(chrome);
        }
        driver = new ChromeDriver(options);
        if (null != home) {
            driver.get(home);
        }
    }

    @Override
    public Object run(Context context) {
        try {
            driver.get(context.getUrl());
            context.body = driver.getPageSource();
            return context.getBody();
        } finally {
            driver.close();
        }
    }
}
