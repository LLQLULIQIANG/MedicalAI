package org.example.ai.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * 创建上下文查询增强器的工厂
 * 当知识库内容有限，查询不到相关内容，默认内容emptyContextPromptTemplate
 * 可以用在异常处理逻辑中
 */
public class MedicalAppContextualQueryAugmenterFactory {

    public static ContextualQueryAugmenter createInstance() {
        PromptTemplate emptyContextPromptTemplate = new PromptTemplate("""
                你应该输出下面的内容：
                抱歉，我只能回健康爱相关的问题，别的没办法帮到您哦，
                有问题可以线下就医
                """);
        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(false)//如果是true的话，就不会回复定制的内容了，会用ai大模型内容
                .emptyContextPromptTemplate(emptyContextPromptTemplate)
                .build();
    }
}
