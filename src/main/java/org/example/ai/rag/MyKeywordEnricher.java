package org.example.ai.rag;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 基于 AI 的文档元信息增强器（为文档补充元信息）。
 * 未配置 Key 或调用失败时退回原文档，避免仅此一步阻断启动（后续向量嵌入仍需要有效 Key）。
 */
@Slf4j
@Component
public class MyKeywordEnricher {

    @Resource
    private ChatModel dashscopeChatModel;

    /** 阿里云百炼 / DashScope：https://dashscope.console.aliyun.com/ */
    @Value("${spring.ai.dashscope.api-key:}")
    private String dashscopeApiKey;

    public List<Document> enrichDocuments(List<Document> documents) {
        if (dashscopeApiKey == null || dashscopeApiKey.isBlank()) {
            log.warn("已跳过关键词增强：未配置 API Key。请在运行配置或环境中设置 DASHSCOPE_API_KEY（对应 spring.ai.dashscope.api-key）。");
            return documents;
        }
        try {
            KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(dashscopeChatModel, 5);
            return enricher.apply(documents);
        } catch (Exception e) {
            log.warn("关键词增强调用失败（如 InvalidApiKey、欠费、网络等），使用原始文档继续: {}", e.getMessage());
            return documents;
        }
    }
}
