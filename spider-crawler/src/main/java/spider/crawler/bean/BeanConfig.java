package spider.crawler.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spider.ext.handler.PlaywrightHandler;

/**
 * @author max.hu  @date 2024/12/24
 **/
@Configuration
public class BeanConfig {

    @Bean
    public PlaywrightHandler jinPlaywrightBean() {
        return PlaywrightHandler.of("https://www.jin10.com", "#jin_flash_list"
                , "http://localhost:8080/callback/jin");
    }
}
