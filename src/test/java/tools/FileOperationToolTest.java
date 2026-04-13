package tools;

import org.example.ai.tools.FileOperationTool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileOperationToolTest {

    private static final String FILE_NAME = "你好AI.txt";
    private static final String CONTENT = "你好";

    @Test
    @Order(1)
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String result = fileOperationTool.writeFile(FILE_NAME, CONTENT);
        Assertions.assertTrue(
                result.contains("successfully"),
                () -> "期望写入成功，实际返回: " + result
        );
    }

    @Test
    @Order(2)
    void readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String result = fileOperationTool.readFile(FILE_NAME);
        Assertions.assertEquals(CONTENT, result, () -> "期望读到刚写入的内容，实际返回: " + result);
    }
}
