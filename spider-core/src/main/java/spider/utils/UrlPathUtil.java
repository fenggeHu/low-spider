package spider.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import java.util.HashMap;
import java.util.Map;

/**
 * 解析动态、含变量的url
 *
 * @author max.hu  @date 2024/01/22
 **/
public class UrlPathUtil {

    /**
     * 使用${var}包含变量
     * eg: https://xxxxx.github.io/reposity/${id}
     *
     * @param uri
     * @param vars
     * @return
     */
    public static String parse(String uri, Map<String, Object> vars) {
        CompiledTemplate template = TemplateCompiler.compileTemplate(uri);
        Object result = TemplateRuntime.execute(template, vars);
        return null == result ? null : String.valueOf(result);
    }

    /**
     * key/value 必须逐组出现，否则抛异常
     *
     * @param uri
     * @param kvs key/value ...
     * @return
     */
    public static String parse(String uri, String... kvs) {
        Map<String, Object> vars = new HashMap<>();
        int i = 0;
        while (i < kvs.length) {
            String key = kvs[i++];
            String value = kvs[i++];
            vars.put(key, value);
        }
        return parse(uri, vars);
    }

    /**
     * 使用键值对 - 方便替换一个变量的情况
     *
     * @param uri
     * @param pair
     * @return
     */
    public static String parse(String uri, Pair<String, Object> pair) {
        CompiledTemplate template = TemplateCompiler.compileTemplate(uri);
        Object result = TemplateRuntime.execute(template, pair);
        return null == result ? null : String.valueOf(result);
    }
}
