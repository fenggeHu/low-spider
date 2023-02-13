package spider.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengge.hu  @Date 2022/9/15
 **/
@Data
@Builder
public class ExcelValue {
    private String[] header;
    private List<String[]> values;

    /**
     * 按列序号取整列
     *
     * @param index
     * @return
     */
    public List<String> get(int index) {
        List<String> ret = new ArrayList<>(values.size());
        if (index < 0) {
            return ret;
        }

        for (String[] sa : values) {
            ret.add(sa[index]);
        }
        return ret;
    }

    /**
     * 按列名获取整列
     *
     * @param headerName
     * @return
     */
    public List<String> get(String headerName) {
        if (null == header) {
            return get(-1);
        }
        int index = -1;
        for (int i = 0; i < header.length; i++) {
            if (headerName.equals(header[i])) {
                index = i;
                break;
            }
        }

        return get(index);
    }
}
