package spider.crawler.dto;

import lombok.Data;

/**
 * @author max.hu  @date 2024/12/24
 **/
@Data
public class Item {
    // 目标
    String target;  // target url
    String name;    // html attribute / properties
    // 内容
    String title;
    String content;
    boolean important;
}
