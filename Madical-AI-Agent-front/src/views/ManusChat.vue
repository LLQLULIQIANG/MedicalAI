<script setup>
import { ref } from 'vue'
import ChatRoom from '../components/ChatRoom.vue'
import { consumeSse, manusSseUrl } from '../api/sseStream'

const messages = ref([])
const sending = ref(false)
const error = ref('')

async function onSend(text) {
  error.value = ''
  messages.value.push({ role: 'user', content: text })
  messages.value.push({ role: 'assistant', content: '' })

  const assistantIndex = messages.value.length - 1
  sending.value = true

  try {
    const url = manusSseUrl(text)
    await consumeSse(url, {
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
    title="AI 超级智能体"
    subtitle="通过 SSE 调用 doChatWithManus"
    :messages="messages"
    :sending="sending"
    :error="error"
    @send="onSend"
  />
</template>
