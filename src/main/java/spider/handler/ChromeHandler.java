package spider.handler;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import spider.base.Context;

/**
 * Description: selenium chrome
 * @author fengge.hu  @Date 2022/9/14
 **/
public class ChromeHandler implements Handler {
    private static final WebDriver driver;

    static {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); //无浏览器模式
        driver = new ChromeDriver(options);
    }

    @Override
    public void run(Context context) {
        try {
            driver.get(context.getUrl());
            context.body = driver.getPageSource();
        } finally {
            driver.close();
        }
    }
}
