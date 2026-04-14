package app;

import jakarta.annotation.Resource;
import org.example.ai.app.MedicalAIApp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class AppTest {

    @Resource
    private MedicalAIApp medicalAIApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是LLQ";
        String answer = medicalAIApp.doChat(message, chatId);
        // 第二轮
        message = "我想了解心脏病有什么症状？";
        answer = medicalAIApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "心脏病有什么症状？";
        answer = medicalAIApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是程序员LLQ";
        MedicalAIApp.MedicalReport medicalReport = medicalAIApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(medicalReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "心脏病有什么症状？";
        String answer = medicalAIApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("心脏病有什么症状？");

        // 测试网页抓取：恋爱案例分析
        testMessage("心脏病有什么症状？");

        // 测试资源下载：图片下载
        testMessage("心脏病有什么症状？");

        // 测试终端操作：执行代码
        testMessage("心脏病有什么症状？");

        // 测试文件操作：保存用户档案
        testMessage("心脏病有什么症状？");

        // 测试 PDF 生成
        testMessage("心脏病有什么症状？");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = medicalAIApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        // 测试地图 MCP
//        String message = "心脏病有什么症状？";
//        String answer =  loveApp.doChatWithMcp(message, chatId);
//        Assertions.assertNotNull(answer);
        // 测试图片搜索 MCP
        String message = "心脏病有什么症状？";
        String answer =  medicalAIApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }
}
