package spider.crawler.dto;

import lombok.Data;

/**
 * @author max.hu  @date 2024/12/24
 **/
@Data
public class Item {
    String url;
    String name;
    String title;
    String content;
    boolean important;
}