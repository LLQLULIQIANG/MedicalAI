package org.example.ai.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 向量数据库配置（初始化基于内存的向量数据库 Bean）
 */
//@Configuration是 Spring 框架​ 中一个类级别的注解，用于标记一个类为配置类。它的核心作用是替代传统的 XML 配置文件，通过 Java 代码的方式定义和配置 Spring Bean。
@Configuration
public class MedicalAppVectorStoreConfig {

    @Resource
    private MedicalAppDocumentLoader medicalAppDocumentLoader;

//    @Resource
//    private MyTokenTextSplitter myTokenTextSplitter;

    @Resource
    private MyKeywordEnricher myKeywordEnricher;

    @Value("${spring.ai.dashscope.api-key:}")
    private String dashscopeApiKey;

    @Bean("medicalAppVectorStore")
    @Primary
    public VectorStore medicalAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        if (dashscopeApiKey == null || dashscopeApiKey.isBlank()) {
            throw new IllegalStateException(
                    "未配置 DashScope API Key，无法构建医疗知识库向量（嵌入模型需联网鉴权）。"
                            + "请在 IntelliJ「运行配置 → 环境变量」添加 DASHSCOPE_API_KEY=你的sk-密钥，"
                            + "或在终端执行 export DASHSCOPE_API_KEY=... 后再启动。"
                            + "（application.yaml 中 spring.ai.dashscope.api-key 已绑定该环境变量）");
        }
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        // 加载文档
        List<Document> documentList = medicalAppDocumentLoader.loadMarkdowns();
        // 自主切分文档
//        List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documentList);
        // 自动补充关键词元信息
        List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(documentList);
        simpleVectorStore.add(enrichedDocuments);
        return simpleVectorStore;
    }
}
