<script setup>
import { ref, watch, nextTick, onMounted, computed } from 'vue'
import { RouterLink } from 'vue-router'

const props = defineProps({
  title: { type: String, required: true },
  subtitle: { type: String, default: '' },
  chatId: { type: String, default: '' },
  messages: {
    type: Array,
    default: () => [],
  },
  sending: { type: Boolean, default: false },
  error: { type: String, default: '' },
})

const emit = defineEmits(['send'])

const input = ref('')
const listEl = ref(null)
const autoScrollEnabled = ref(true)

const isEmpty = computed(() => !props.messages.length)

function isNearBottom(el, threshold = 2) {
  if (!el) return true
  const distanceToBottom = el.scrollHeight - el.scrollTop - el.clientHeight
  return distanceToBottom <= threshold
}

function scrollToBottom() {
  nextTick(() => {
    const el = listEl.value
    if (el && autoScrollEnabled.value) el.scrollTop = el.scrollHeight
  })
}

function onScroll() {
  const el = listEl.value
  if (!el) return
  // 用户上滑查看历史内容时，停止自动吸底；回到底部后恢复。
  autoScrollEnabled.value = isNearBottom(el)
}

function onWheel(e) {
  // 只要用户有“上滚”意图，立刻关闭自动跟随，避免第一段流式输出把视图抢回底部。
  if (e.deltaY < 0) autoScrollEnabled.value = false
}

watch(
  () => props.messages.length,
  () => scrollToBottom(),
)

watch(
  () => props.messages[props.messages.length - 1]?.content,
  () => scrollToBottom(),
)

watch(
  () => props.sending,
  (isSending) => {
    const el = listEl.value
    if (!el) return
    if (isSending) {
      // 开始流式输出时，按“当前是否在底部”决定是否自动跟随。
      // 不在底部就锁定，整轮输出都不抢用户滚动位置。
      autoScrollEnabled.value = isNearBottom(el)
    }
  },
)

onMounted(scrollToBottom)

function onSubmit() {
  const text = input.value.trim()
  if (!text || props.sending) return
  emit('send', text)
  input.value = ''
}
</script>

<template>
  <div class="room">
    <header class="room__bar">
      <div class="room__bar-inner">
        <RouterLink class="room__back" to="/">
          <span class="room__back-icon" aria-hidden="true">‹</span>
          返回首页
        </RouterLink>
        <div class="room__meta">
          <h1>{{ title }}</h1>
          <p v-if="subtitle" class="room__sub">{{ subtitle }}</p>
          <p v-if="chatId" class="room__id">
            会话 <code>{{ chatId }}</code>
          </p>
        </div>
      </div>
    </header>

    <div ref="listEl" class="room__scroll" @scroll="onScroll" @wheel.passive="onWheel">
      <div class="room__messages">
        <div v-if="isEmpty" class="room__empty">
          <p class="room__empty-title">开始对话</p>
          <p class="room__empty-hint">在下方输入健康问题或任务描述，支持流式输出。</p>
        </div>

        <div
          v-for="(m, i) in messages"
          :key="i"
          class="row"
          :class="m.role === 'user' ? 'row--user' : 'row--ai'"
        >
          <div class="avatar" :class="m.role === 'user' ? 'avatar--user' : 'avatar--ai'" aria-hidden="true">
            {{ m.role === 'user' ? '我' : 'AI' }}
          </div>
          <div class="bubble-wrap">
            <div class="bubble" :class="m.role === 'user' ? 'bubble--user' : 'bubble--ai'">
              <span class="bubble__label">{{ m.role === 'user' ? '我' : '助手' }}</span>
              <div class="bubble__text">{{ m.content || (sending && i === messages.length - 1 ? '…' : '') }}</div>
            </div>
          </div>
        </div>

        <p v-if="error" class="room__error" role="alert">{{ error }}</p>
      </div>
    </div>

    <div class="room__dock">
      <form class="room__form" @submit.prevent="onSubmit">
        <input
          v-model="input"
          type="text"
          autocomplete="off"
          placeholder="输入消息，Enter 发送"
          :disabled="sending"
          class="room__field"
        />
        <button type="submit" class="room__send" :disabled="sending || !input.trim()">
          <span v-if="sending" class="room__send-pulse" />
          <span>{{ sending ? '生成中' : '发送' }}</span>
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.room {
  height: 100dvh;
  display: flex;
  flex-direction: column;
  background: transparent;
  overflow: hidden;
}

.room__bar {
  flex-shrink: 0;
  padding: 0.75rem 1rem;
  border-bottom: 1px solid var(--border);
  background: var(--surface);
  backdrop-filter: blur(16px);
  position: sticky;
  top: 0;
  z-index: 10;
}

.room__bar-inner {
  max-width: 720px;
  margin: 0 auto;
}

.room__back {
  display: inline-flex;
  align-items: center;
  gap: 0.2rem;
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--teal-700);
  text-decoration: none;
  margin-bottom: 0.65rem;
  padding: 0.25rem 0;
  transition: color 0.15s ease;
}

.room__back:hover {
  color: var(--accent);
}

.room__back-icon {
  font-size: 1.2rem;
  line-height: 1;
  opacity: 0.85;
}

.room__meta h1 {
  margin: 0;
  font-size: 1.2rem;
  font-weight: 750;
  letter-spacing: -0.02em;
  color: var(--text);
}

.room__sub {
  margin: 0.35rem 0 0;
  font-size: 0.82rem;
  color: var(--muted);
  line-height: 1.45;
}

.room__id {
  margin: 0.5rem 0 0;
  font-size: 0.75rem;
  color: var(--muted);
}

.room__id code {
  font-size: 0.8em;
  padding: 0.15rem 0.45rem;
  border-radius: 8px;
  background: var(--code-bg);
  color: var(--slate-700);
  word-break: break-all;
}

.room__scroll {
  flex: 1;
  overflow-y: auto;
  padding: 1rem 1rem 0.5rem;
}

.room__messages {
  max-width: 720px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding-bottom: 1rem;
}

.room__empty {
  text-align: center;
  padding: 2.5rem 1rem 1.5rem;
  border-radius: var(--radius-lg);
  border: 1px dashed var(--border);
  background: rgba(255, 255, 255, 0.5);
}

.room__empty-title {
  margin: 0 0 0.4rem;
  font-size: 1rem;
  font-weight: 700;
  color: var(--slate-700);
}

.room__empty-hint {
  margin: 0;
  font-size: 0.88rem;
  color: var(--muted);
  line-height: 1.5;
}

.row {
  display: flex;
  gap: 0.65rem;
  align-items: flex-end;
}

.row--user {
  flex-direction: row-reverse;
}

.avatar {
  flex-shrink: 0;
  width: 2.1rem;
  height: 2.1rem;
  border-radius: var(--radius-pill);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.65rem;
  font-weight: 800;
  letter-spacing: 0.02em;
}

.avatar--user {
  background: linear-gradient(145deg, var(--teal-500), var(--teal-600));
  color: #fff;
  box-shadow: 0 4px 12px rgba(13, 148, 136, 0.35);
}

.avatar--ai {
  background: linear-gradient(145deg, var(--indigo-500), var(--indigo-600));
  color: #fff;
  box-shadow: 0 4px 12px rgba(79, 70, 229, 0.3);
}

.bubble-wrap {
  max-width: min(100% - 3rem, 560px);
  min-width: 0;
}

.bubble {
  border-radius: 16px;
  padding: 0.7rem 1rem;
  border: 1px solid var(--border);
  box-shadow: var(--shadow-sm);
}

.bubble--user {
  background: var(--user-bubble);
  border-color: rgba(13, 148, 136, 0.18);
  border-bottom-right-radius: 6px;
}

.bubble--ai {
  background: var(--ai-bubble);
  border-left: 3px solid var(--indigo-500);
  border-bottom-left-radius: 6px;
}

.bubble__label {
  display: block;
  font-size: 0.65rem;
  font-weight: 700;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: var(--muted);
  margin-bottom: 0.35rem;
}

.bubble__text {
  white-space: pre-wrap;
  word-break: break-word;
  font-size: 0.94rem;
  line-height: 1.55;
  color: var(--text);
}

.room__error {
  color: #b91c1c;
  font-size: 0.86rem;
  margin: 0;
  padding: 0.65rem 0.85rem;
  border-radius: var(--radius-md);
  background: #fef2f2;
  border: 1px solid #fecaca;
}

.room__dock {
  flex-shrink: 0;
  position: sticky;
  bottom: 0;
  z-index: 12;
  padding: 0.75rem 1rem calc(0.85rem + env(safe-area-inset-bottom, 0));
  border-top: 1px solid var(--border);
  background: var(--surface);
  backdrop-filter: blur(16px);
}

.room__form {
  max-width: 720px;
  margin: 0 auto;
  display: flex;
  gap: 0.65rem;
  align-items: center;
}

.room__field {
  flex: 1;
  border: 1px solid var(--border);
  border-radius: var(--radius-pill);
  padding: 0.72rem 1.1rem;
  font-size: 0.95rem;
  background: var(--input-bg);
  color: var(--text);
  transition:
    border-color 0.15s ease,
    box-shadow 0.15s ease;
}

.room__field::placeholder {
  color: #94a3b8;
}

.room__field:focus {
  outline: none;
  border-color: rgba(13, 148, 136, 0.45);
  box-shadow: 0 0 0 3px var(--accent-soft);
}

.room__field:disabled {
  opacity: 0.65;
}

.room__send {
  position: relative;
  border: none;
  border-radius: var(--radius-pill);
  padding: 0.72rem 1.35rem;
  font-weight: 700;
  font-size: 0.88rem;
  cursor: pointer;
  color: #fff;
  background: linear-gradient(135deg, var(--teal-500), var(--teal-600));
  box-shadow: 0 4px 14px rgba(13, 148, 136, 0.4);
  transition:
    transform 0.15s ease,
    box-shadow 0.15s ease,
    opacity 0.15s ease;
}

.room__send:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(13, 148, 136, 0.45);
}

.room__send:active:not(:disabled) {
  transform: translateY(0);
}

.room__send:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  box-shadow: none;
}

.room__send-pulse {
  position: absolute;
  inset: 0;
  border-radius: inherit;
  background: rgba(255, 255, 255, 0.2);
  animation: pulse 1.2s ease-in-out infinite;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
}
</style>
