import { API_BASE } from '../config'

/**
 * 读取 Spring SSE（text/event-stream），按事件回调 data 文本。
 * 浏览器端流式请求使用 fetch；axios 不适合 SSE。
 */
export async function consumeSse(url, { onData, signal } = {}) {
  const res = await fetch(url, {
    method: 'GET',
    headers: { Accept: 'text/event-stream' },
    signal,
  })

  if (!res.ok) {
    const text = await res.text().catch(() => '')
    throw new Error(text || `HTTP ${res.status}`)
  }

  const reader = res.body?.getReader()
  if (!reader) throw new Error('响应不支持流式读取')

  const decoder = new TextDecoder()
  let carry = ''

  function flushEvent(raw) {
    const lines = raw.replace(/\r\n/g, '\n').split('\n')
    const dataLines = lines
      .filter((l) => l.startsWith('data:'))
      .map((l) => l.replace(/^data:\s?/, ''))
    if (dataLines.length) {
      if (onData) onData(dataLines.join('\n'))
      return
    }
    const trimmed = raw.trim()
    if (trimmed && onData) onData(trimmed)
  }

  while (true) {
    const { done, value } = await reader.read()
    if (done) break
    carry += decoder.decode(value, { stream: true })
    carry = carry.replace(/\r\n/g, '\n')

    let idx
    while ((idx = carry.indexOf('\n\n')) !== -1) {
      const raw = carry.slice(0, idx)
      carry = carry.slice(idx + 2)
      flushEvent(raw)
    }
  }

  if (carry.trim()) flushEvent(carry)
}

export function loveAppSseUrl(message, chatId) {
  const q = new URLSearchParams({ message, chatId })
  return `${API_BASE}/ai/love_app/chat/sse?${q}`
}

export function manusSseUrl(message) {
  const q = new URLSearchParams({ message })
  return `${API_BASE}/ai/manus/chat?${q}`
}
