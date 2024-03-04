package spider;

import lombok.Data;

/**
 * @author max.hu  @date 2024/03/04
 **/
@Data
public class SpiderResponse<T> {

    private T data;
}
