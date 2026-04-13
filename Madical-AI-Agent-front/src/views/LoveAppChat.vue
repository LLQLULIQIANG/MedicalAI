<script setup>
import { ref, onMounted } from 'vue'
import ChatRoom from '../components/ChatRoom.vue'
import { consumeSse, loveAppSseUrl } from '../api/sseStream'

const chatId = ref('')
const messages = ref([])
const sending = ref(false)
const error = ref('')

onMounted(() => {
  chatId.value =
    typeof crypto !== 'undefined' && crypto.randomUUID
      ? crypto.randomUUID()
      : `chat-${Date.now()}-${Math.random().toString(16).slice(2)}`
})

async function onSend(text) {
  error.value = ''
  messages.value.push({ role: 'user', content: text })
  messages.value.push({ role: 'assistant', content: '' })

  const assistantIndex = messages.value.length - 1
  sending.value = true
  const ac = new AbortController()

  try {
    const url = loveAppSseUrl(text, chatId.value)
    await consumeSse(url, {
      signal: ac.signal,
      onData: (chunk) => {
        const cur = messages.value[assistantIndex]
        messages.value[assistantIndex] = {
          ...cur,
          content: cur.content + chunk,
        }
      },
    })
  } catch (e) {
    if (e.name === 'AbortError') return
    error.value = e.message || String(e)
    if (!messages.value[assistantIndex].content) {
      messages.value[assistantIndex].content = '（流式输出失败，请检查后端与网络）'
    }
  } finally {
    sending.value = false
  }
}
</script>

<template>
  <ChatRoom
    title="AI 医疗大师"
    subtitle="SSE · 医疗知识库 RAG + 会话记忆"
    :chat-id="chatId"
    :messages="messages"
    :sending="sending"
    :error="error"
    @send="onSend"
  />
</template>
