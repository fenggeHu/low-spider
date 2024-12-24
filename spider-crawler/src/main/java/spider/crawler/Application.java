package spider.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author max.hu  @date 2024/12/24
 **/
@SpringBootApplication(scanBasePackages = {"spider.crawler"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
