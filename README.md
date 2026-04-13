# MedicalAI

一个可本地运行的医疗 AI 项目，包含：

- Spring Boot 后端（RAG、工具调用、SSE 流式输出、智能体）
- Vue 3 + Vite 前端（双页面聊天 UI）
- 可选 MCP 工具链扩展（默认关闭）

## 功能概览

- **AI 医疗大师**：基于文档知识库 + RAG 的对话能力（支持 SSE 流式输出）
- **AI 超级智能体（Manus）**：可自主规划并调用工具（文件、网页抓取、搜索、下载、终止等）
- **会话隔离**：支持 `chatId` 多会话记忆
- **接口文档**：集成 Knife4j / OpenAPI

## 项目结构

- `src/main/java/org/example/ai`：后端主代码
  - `controller`：REST + SSE 接口
  - `app`：核心 AI 应用编排
  - `agent`：Manus 智能体执行框架
  - `rag`：文档加载、向量库、检索增强
  - `tools`：可被 Agent 调用的工具集合
- `src/main/resources`：后端配置与知识库文档
- `Madical-AI-Agent-front`：前端项目（Vue 3 + Vite）

## 环境要求

- JDK 21
- Maven 3.9+
- Node.js 18+（前端）

## 关键环境变量（必须）

> 请使用环境变量，不要把密钥写入仓库。

- `DASHSCOPE_API_KEY`：阿里云百炼 / DashScope Key（后端大模型与嵌入必需）
- `SEARCH_API_KEY`：网页搜索工具可选 Key（不用可留空）
- `AMAP_MAPS_API_KEY`：MCP 高德地图服务可选 Key（不用可留空）

## 本地启动

### 1) 启动后端

```bash
export DASHSCOPE_API_KEY="你的真实sk"
export SEARCH_API_KEY="可选"
cd /Users/luliqiang/AI
mvn spring-boot:run
```

默认地址：

- 服务：`http://localhost:8123`
- 上下文：`/api`
- Swagger：`http://localhost:8123/api/swagger-ui.html`

### 2) 启动前端

```bash
cd /Users/luliqiang/AI/Madical-AI-Agent-front
npm install
npm run dev
```

前端开发环境通过 Vite 代理转发到后端 `/api`。

## 主要接口

- `GET /api/ai/love_app/chat/sync`
- `GET /api/ai/love_app/chat/sse`
- `GET /api/ai/manus/chat`

参数示例：

- `message`：用户问题
- `chatId`：会话 ID（love_app 接口建议传）

## MCP 说明（可选）

默认已关闭 MCP 客户端，避免本地未启动 MCP 服务时阻断后端启动。

- 默认配置：`application.yaml` 中 `spring.ai.mcp.client.enabled=false`
- 需要时启用：追加 profile `mcp`，并确保 `localhost:8127` 或 `mcp-servers.json` 所配置服务可连接。

## 安全说明

- 本仓库已移除硬编码密钥与个人口令
- 请使用环境变量注入敏感信息
- 若曾泄露 Key，请立即在控制台作废并重建

