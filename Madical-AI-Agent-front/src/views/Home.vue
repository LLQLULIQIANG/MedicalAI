<script setup>
import { RouterLink } from 'vue-router'
</script>

<template>
  <div class="home">
    <div class="home__glow" aria-hidden="true" />

    <header class="home__header">
      <div class="home__brand">
        <span class="home__logo" aria-hidden="true">
          <svg viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect width="40" height="40" rx="12" fill="url(#g)" />
            <path
              d="M20 11v18M14 17h12M14 23h12"
              stroke="white"
              stroke-width="2"
              stroke-linecap="round"
            />
            <defs>
              <linearGradient id="g" x1="8" y1="4" x2="36" y2="36" gradientUnits="userSpaceOnUse">
                <stop stop-color="#0d9488" />
                <stop offset="1" stop-color="#4f46e5" />
              </linearGradient>
            </defs>
          </svg>
        </span>
        <div>
          <p class="home__eyebrow">Med · AI</p>
          <h1>医疗 AI 应用中心</h1>
        </div>
      </div>
      <p class="home__sub">选择下方入口，开始对话或调用智能体能力</p>
    </header>

    <div class="cards">
      <RouterLink class="card card--love" to="/love-app">
        <div class="card__accent" />
        <div class="card__body">
          <span class="card__badge">RAG 知识库</span>
          <h2>AI 医疗大师</h2>
          <p>流式对话 · 医疗文档检索 · 会话记忆隔离</p>
          <ul class="card__tags">
            <li>SSE</li>
            <li>百炼</li>
          </ul>
        </div>
        <span class="card__arrow" aria-hidden="true">→</span>
      </RouterLink>

      <RouterLink class="card card--manus" to="/manus">
        <div class="card__accent" />
        <div class="card__body">
          <span class="card__badge">工具编排</span>
          <h2>AI 超级智能体</h2>
          <p>多步推理 · Manus 流式输出 · 工具调用</p>
          <ul class="card__tags">
            <li>Agent</li>
            <li>Tools</li>
          </ul>
        </div>
        <span class="card__arrow" aria-hidden="true">→</span>
      </RouterLink>
    </div>

    <p class="home__foot">后端默认 <code>/api</code> · 端口与代理请对照 Vite 配置</p>
  </div>
</template>

<style scoped>
.home {
  position: relative;
  min-height: 100vh;
  padding: clamp(2rem, 5vw, 3.5rem) 1.5rem 2.5rem;
  max-width: 920px;
  margin: 0 auto;
  overflow-x: hidden;
}

.home__glow {
  position: fixed;
  inset: -20% -10% auto;
  height: 50vh;
  max-height: 420px;
  background: radial-gradient(ellipse 70% 60% at 50% 0%, var(--accent-glow), transparent 70%);
  pointer-events: none;
  z-index: 0;
}

.home__header {
  position: relative;
  z-index: 1;
  margin-bottom: 2.25rem;
}

.home__brand {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 0.75rem;
}

.home__logo {
  display: flex;
  flex-shrink: 0;
  filter: drop-shadow(0 8px 20px rgba(13, 148, 136, 0.35));
}

.home__logo svg {
  width: 48px;
  height: 48px;
}

.home__eyebrow {
  margin: 0 0 0.2rem;
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: var(--teal-600);
}

.home__header h1 {
  font-size: clamp(1.65rem, 4vw, 2rem);
  font-weight: 750;
  letter-spacing: -0.03em;
  line-height: 1.2;
  color: var(--text);
  margin: 0;
  background: linear-gradient(120deg, var(--slate-900) 0%, var(--slate-700) 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.home__sub {
  margin: 0;
  max-width: 36rem;
  color: var(--muted);
  font-size: 0.98rem;
  line-height: 1.55;
}

.cards {
  position: relative;
  z-index: 1;
  display: grid;
  gap: 1.1rem;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
}

.card {
  position: relative;
  display: flex;
  align-items: stretch;
  gap: 0.5rem;
  padding: 0;
  border-radius: var(--radius-lg);
  text-decoration: none;
  color: inherit;
  border: 1px solid var(--border);
  background: var(--surface);
  backdrop-filter: blur(12px);
  box-shadow: var(--shadow);
  overflow: hidden;
  transition:
    transform 0.22s cubic-bezier(0.34, 1.2, 0.64, 1),
    box-shadow 0.22s ease,
    border-color 0.2s ease;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: rgba(13, 148, 136, 0.25);
}

.card--manus:hover {
  border-color: rgba(79, 70, 229, 0.28);
}

.card__accent {
  width: 5px;
  flex-shrink: 0;
  background: linear-gradient(180deg, var(--teal-500), var(--teal-600));
}

.card--manus .card__accent {
  background: linear-gradient(180deg, var(--indigo-500), var(--indigo-600));
}

.card__body {
  flex: 1;
  padding: 1.35rem 0.5rem 1.35rem 0;
  min-width: 0;
}

.card__badge {
  display: inline-block;
  font-size: 0.68rem;
  font-weight: 700;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: var(--teal-600);
  margin-bottom: 0.55rem;
}

.card--manus .card__badge {
  color: var(--indigo-600);
}

.card h2 {
  margin: 0 0 0.45rem;
  font-size: 1.22rem;
  font-weight: 720;
  letter-spacing: -0.02em;
}

.card p {
  margin: 0;
  font-size: 0.9rem;
  color: var(--muted);
  line-height: 1.5;
}

.card__tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.4rem;
  margin: 0.85rem 0 0;
  padding: 0;
  list-style: none;
}

.card__tags li {
  font-size: 0.72rem;
  font-weight: 600;
  padding: 0.2rem 0.55rem;
  border-radius: var(--radius-pill);
  background: var(--slate-100);
  color: var(--slate-700);
}

.card__arrow {
  align-self: center;
  padding: 0 1rem 0 0.25rem;
  font-size: 1.25rem;
  color: var(--slate-500);
  opacity: 0.65;
  transition:
    transform 0.2s ease,
    opacity 0.2s ease;
}

.card:hover .card__arrow {
  transform: translateX(4px);
  opacity: 1;
  color: var(--teal-600);
}

.card--manus:hover .card__arrow {
  color: var(--indigo-600);
}

.home__foot {
  position: relative;
  z-index: 1;
  margin: 2.5rem 0 0;
  font-size: 0.8rem;
  color: var(--muted);
  text-align: center;
}

.home__foot code {
  font-size: 0.85em;
  padding: 0.12rem 0.4rem;
  border-radius: 6px;
  background: var(--code-bg);
  color: var(--slate-700);
}
</style>
