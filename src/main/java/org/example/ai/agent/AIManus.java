package org.example.ai.agent;


import org.example.ai.customadvisor.MyLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;

/**
 * 医疗场景超级智能体（自主规划与工具调用；每次请求应 new 实例以保证会话隔离）。
 */
public class AIManus extends ToolCallAgent {

    public AIManus(ToolCallback[] allTools, ChatModel dashscopeChatModel) {
        super(allTools);
        this.setName("MedicalManus");
        String SYSTEM_PROMPT = """
                你是面向中文用户的医疗与科研辅助智能体 MedicalManus（非执业医师）。
                在健康科普、就诊准备、文献与资料整理等任务上主动使用工具；回答需准确、可核对，并明确医学决策须由医生做出。
                遇到可能急症场景时优先提示立即就医。
                """;
        this.setSystemPrompt(SYSTEM_PROMPT);
        String NEXT_STEP_PROMPT = """
                根据用户需求，主动选择最合适的工具或工具组合；复杂任务可拆解为多步执行。
                每次使用工具后，用简洁中文说明结果与下一步建议。
                需要结束对话时，调用 `terminate` 工具。
                """;
        this.setNextStepPrompt(NEXT_STEP_PROMPT);
        this.setMaxSteps(20);
        // 初始化 AI 对话客户端
        ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultAdvisors(new MyLoggerAdvisor())
                .build();
        this.setChatClient(chatClient);
    }
}
