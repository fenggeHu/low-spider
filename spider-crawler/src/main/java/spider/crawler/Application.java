package spider.crawler;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import spider.crawler.bean.PlaywrightBean;

/**
 * @author max.hu  @date 2024/12/24
 **/
@SpringBootApplication(scanBasePackages = {"spider.crawler"})
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private PlaywrightBean playwrightBean;
    @Override
    public void run(String... args) throws Exception {
        playwrightBean.run();
    }
}
