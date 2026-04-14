package org.example.ai.demo.invoke;

import org.example.ai.config.ApiKeySiteRegistry;

public interface TestApiKey {
    /** 阿里云百炼 / DashScope：https://dashscope.console.aliyun.com/ */
    String API_KEY = System.getenv(ApiKeySiteRegistry.ENV_DASHSCOPE_API_KEY);
}
