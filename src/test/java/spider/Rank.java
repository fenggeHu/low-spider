package spider;

import lombok.Builder;
import lombok.Data;

/**
 * Description: 排名
 * @author fengge.hu  @Date 2022/9/13
 **/
@Data
@Builder
public class Rank {
    private int rank;
    private String symbol;
    private String name;
    private String datetime;
    private Double close;
    private Long volume;
    private Long turnover;
    //
}
