# MedicalAI（一位居家医生）

基于 **Spring Boot 3 + Spring AI / 阿里云 DashScope** 的医疗问答与 RAG 能力后端，以及 **Vue 3 + Vite 5** 的 Web 前端。仓库地址：[https://github.com/LLQLULIQIANG/MedicalAI](https://github.com/LLQLULIQIANG/MedicalAI)

---

## 演示视频

录屏文件已放在仓库内（约 45MB，QuickTime `.mov`）。在 GitHub 网页上可直接用下方播放器观看；若无法播放，可 [在仓库中打开 `docs/demo-screen-recording-2026-04-14.mov`](./docs/demo-screen-recording-2026-04-14.mov) 或克隆后本地播放。

<video src="docs/demo-screen-recording-2026-04-14.mov" controls width="100%" poster=""></video>

> 说明：部分浏览器对 README 内嵌视频的兼容性不一致；本地查看时可直接双击 `docs/demo-screen-recording-2026-04-14.mov`。

---

## 功能概览

- **对话**：与医疗场景 Agent 多轮对话（流式输出由前端配合展示）。
- **RAG / 向量检索**：集成向量存储与文档检索（默认本地可使用 H2 内存库便于启动；生产可切换 PostgreSQL 等）。
- **工具扩展**：网页搜索（SearchAPI）、可选 MCP（默认关闭，避免未启动 MCP 服务时阻塞启动）。
- **接口文档**：集成 Swagger / Knife4j，便于联调。

---

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Java 21、Spring Boot 3.4、Spring AI、Spring AI Alibaba（DashScope）、Ollama（可选本地模型）、JDBC / H2 或 PG |
| 前端 | Vue 3.5、Vue Router 4、Vite 5、Axios |
| 构建 | Maven Wrapper、npm |

---

## 目录结构（摘要）

```
MedicalAI/
├── src/main/java/org/example/ai/   # 后端主代码（控制器、Agent、配置等）
├── src/main/resources/             # application*.yml、key-management.yml 等
├── Madical-AI-Agent-front/         # 前端工程（Vite + Vue）
├── docs/                           # 演示录屏等静态资源
├── pom.xml
└── README.md
```

---

## 环境要求

- **JDK 21**（与 `pom.xml` 中 `java.version` 一致）
- **Node.js**：推荐使用 **20 或 22 LTS**（避免在极新 Node 版本上遇到前端工具链边缘问题）
- **Maven**：可使用项目自带的 `./mvnw`

可选：

- **Ollama**：若使用 `application` 中配置的本地 Ollama 模型，需本机安装并拉取对应模型。
- **PostgreSQL**：生产或完整向量场景可在配置中改为真实 PG，而非默认 H2。

---

## 密钥与环境变量（必读）

**请勿将真实 API Key 提交到 Git 仓库。** 本仓库中的 `src/main/resources/key-management.yml` 仅通过环境变量占位，本地与服务器请自行配置：

| 环境变量 | 用途 | 申请/文档 |
|----------|------|-----------|
| `DASHSCOPE_API_KEY` | 阿里云百炼 / DashScope（对话、Embedding 等） | [DashScope 控制台](https://dashscope.console.aliyun.com/) |
| `SEARCHAPI_API_KEY` | 网页搜索工具（可选） | [SearchAPI](https://www.searchapi.io/) |
| `AMAP_MAPS_API_KEY` | 高德地图相关 MCP（可选） | [高德开放平台](https://lbs.amap.com/) |

在 macOS / Linux 终端示例：

```bash
export DASHSCOPE_API_KEY="你的密钥"
# 可选
export SEARCHAPI_API_KEY="你的密钥"
export AMAP_MAPS_API_KEY="你的密钥"
```

Windows 可使用「系统环境变量」或 PowerShell 的 `$env:DASHSCOPE_API_KEY="..."`。

若未配置 DashScope Key，与模型调用相关的功能将无法正常启动或使用。

---

## 本地启动

### 1. 启动后端

在项目根目录：

```bash
./mvnw spring-boot:run
```

默认：

- 服务端口：**8123**
- 上下文路径：**`/api`**（即根地址为 `http://localhost:8123/api`）

主类：`org.example.ai.AiApplication`。

Swagger / Knife4j 入口（具体路径以启动日志与 `application.yaml` 为准）：

- API 文档：`http://localhost:8123/api/v3/api-docs`
- Swagger UI：`http://localhost:8123/api/swagger-ui.html`

### 2. 启动前端

```bash
cd Madical-AI-Agent-front
npm install
npm run dev
```

开发服务器默认多为 `http://127.0.0.1:5173`。`vite.config.js` 已将 **`/api` 代理到 `http://localhost:8123`**，因此前端请求需带 `/api` 前缀时会自动转发到后端，请先确保后端已启动。

生产构建：

```bash
npm run build
npm run preview   # 本地预览构建结果
```

---

## 配置说明（摘要）

- **`application.yaml`**：数据源、DashScope、Ollama、MCP 客户端开关等。默认 **MCP 客户端关闭**（`spring.ai.mcp.client.enabled: false`），避免本机未起 MCP 服务时启动失败。
- **`key-management.yml`**：通过 `spring.config.import` 引入，仅映射 `app.keys.*` 到环境变量，**不包含明文密钥**。
- **`application-local.yml`**：若存在可放个人本地覆盖（该文件已在 `.gitignore` 中，不会被提交）。

生产部署请使用 `application-prod.yml` 或等价方式配置数据库与密钥，切勿在仓库中保存生产密钥。

---

## MCP 与可选组件

- 需要 MCP 时，可在配置中启用客户端，并确保对应 MCP Server 已启动、网络可达。
- 子目录 **`AI-image-search-mcp-server`** 为相关 MCP 工程示例（按需单独构建与运行）。

---

## 常见问题

1. **前端 `npm run dev` 报错**  
   请使用本仓库 `package.json` 锁定的 **Vite 5 + @vitejs/plugin-vue 5**，并优先使用 Node 20/22；删除 `node_modules` 与锁文件后重新 `npm install` 再试。

2. **后端提示 DashScope API Key 未设置**  
   检查是否已导出 `DASHSCOPE_API_KEY`，且与 `key-management.yml` / `application.yaml` 中的引用一致。

3. **大文件克隆较慢**  
   `docs/` 下演示视频约 45MB；若仅需代码可考虑浅克隆：`git clone --depth 1 <url>`。

---

## 开源与许可

若需补充 License，请在仓库根目录添加 `LICENSE` 文件并在此 README 中更新说明。

---

## 相关链接

- 本仓库：[https://github.com/LLQLULIQIANG/MedicalAI](https://github.com/LLQLULIQIANG/MedicalAI)
- Spring AI 文档：[https://docs.spring.io/spring-ai/reference/](https://docs.spring.io/spring-ai/reference/)
