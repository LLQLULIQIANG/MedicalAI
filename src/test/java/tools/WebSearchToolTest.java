package tools;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import org.example.ai.tools.WebSearchTool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WebSearchToolTest {

    @Test
    void searchWeb() {
        String apiKey = loadSearchApiKey();
        Assertions.assertFalse(
                apiKey == null || apiKey.isBlank(),
                "请在 src/main/resources/application.yaml 中配置 search-api.api-key"
        );

        WebSearchTool webSearchTool = new WebSearchTool(apiKey);
        String query = "程序员鱼皮编程导航 codefather.cn";
        String result = webSearchTool.searchWeb(query);
        Assertions.assertNotNull(result);
    }

    private static String loadSearchApiKey() {
        Dict root = YamlUtil.load(ResourceUtil.getUtf8Reader("classpath:application.yaml"));
        Assertions.assertNotNull(root, "application.yaml 解析结果为空");
        Object key = root.getByPath("search-api.api-key");
        return key != null ? key.toString().trim() : null;
    }
}
