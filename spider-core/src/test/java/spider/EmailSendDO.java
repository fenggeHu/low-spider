package spider;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class EmailSendDO {
    private static final long serialVersionUID = 4456431936787149523L;
    // 支持发送多个地址 - 故不做email地址校验
    private String email;
    private String userName;

    private String requestId;
    private Long orgId;
    private String businessType;

    private String language;
    private Map<String, Object> data;
    private String json;
}
