package spider;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Index {
    // symbol
    @SerializedName("id") //gson
    private String symbol;
    private String name;
    private String date;
    private Double price;
    @SerializedName("trading_volume")
    private Long volume;
    // 日期格式
    public String getDate() {
        return null == date ? null : date.replaceAll("/", "-");
    }
}
