package spider.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author fengge.hu  @Date 2022/9/15
 **/
@Data
@Builder
public class ExcelValue {
    private String[] header;
    private List<String[]> values;
}
