package spider.utils;

import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

/**
 * 解析动态、含变量的url
 * @author max.hu  @date 2024/01/22
 **/
public class UrlPathUtil {

    /**
     * 使用${var}包含变量
     * eg: https://xxxxx.github.io/reposity/${id}
     * @param uri
     * @param vars
     * @return
     */
    public static String parse(String uri, Map<String, Object> vars){
        CompiledTemplate template = TemplateCompiler.compileTemplate(uri);
        Object result = TemplateRuntime.execute(template, vars);
        return null == result ? null : String.valueOf(result);
    }
}
