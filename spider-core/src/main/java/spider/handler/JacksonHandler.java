package spider.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import spider.base.Context;
import spider.utils.JacksonUtil;

import java.lang.reflect.Type;

/**
 * @author max.hu  @date 2024/04/29
 **/
public class JacksonHandler implements Handler {
    private final ObjectMapper objectMapper = new ObjectMapper()
            // 设置在反序列化时忽略在JSON字符串中存在，而在Java中不存在的属性
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    /**
     * 提取json结构下的某个节点，再序列化为对象
     * eg: result.data.rows[1].kline
     */
    @Setter
    protected String node;
    @Setter
    protected Type type;

    public JacksonHandler() {
    }

    public JacksonHandler(String node) {
        this.node = node;
    }

    public JacksonHandler(JavaType type) {
        this.type = type;
    }

    public JacksonHandler(String node, Type type) {
        this.node = node;
        this.type = type;
    }

    // 预处理 - 包含一些不规范的json字符串
    protected void preRun(final Context context) {
    }

    @SneakyThrows
    @Override
    public Object run(Context context) {
        if (StringUtils.isBlank(context.getBody())) return null;
        this.init();    // init

        this.preRun(context);

        JsonNode rootNode = objectMapper.readTree(context.getBody());

        // 如果没有指定node或class，返回json对象
        if (StringUtils.isBlank(node) && null == type) {
            context.setResult(rootNode);
            return rootNode;
        }

        // 先解析node
        if (StringUtils.isNotBlank(node)) {
            rootNode = JacksonUtil.getJsonNode(rootNode, node);
        }

        // 序列化class
        if (null != type) {
            context.setResult(JacksonUtil.parse(rootNode, type));
        } else {
            // 没有设置class就返回jsonNode
            context.setResult(rootNode);
        }

        return context.getResult();
    }

}
