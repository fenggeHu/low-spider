package spider.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import spider.ext.handler.PlaywrightHandler;

/**
 * @author max.hu  @date 2024/12/24
 **/
@SpringBootApplication(scanBasePackages = {"spider.crawler"})
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private PlaywrightHandler jinPlaywrightHandler;
    @Override
    public void run(String... args) throws Exception {
        jinPlaywrightHandler.run();
    }
}
