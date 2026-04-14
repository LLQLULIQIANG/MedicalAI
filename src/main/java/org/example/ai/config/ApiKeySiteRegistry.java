package org.example.ai.config;

/**
 * 统一维护项目使用到的密钥环境变量名及官网说明。
 */
public final class ApiKeySiteRegistry {

    private ApiKeySiteRegistry() {
    }

    /** 阿里云百炼 / DashScope（https://dashscope.console.aliyun.com/） */
    public static final String ENV_DASHSCOPE_API_KEY = "DASHSCOPE_API_KEY";

    /** SearchAPI（https://www.searchapi.io/） */
    public static final String ENV_SEARCH_API_KEY = "SEARCH_API_KEY";

    /** 高德开放平台（https://lbs.amap.com/） */
    public static final String ENV_AMAP_MAPS_API_KEY = "AMAP_MAPS_API_KEY";
}
