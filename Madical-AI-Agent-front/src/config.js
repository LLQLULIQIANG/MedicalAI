/** 开发环境走 Vite 代理，避免跨域；生产可改为完整后端地址 */
export const API_BASE =
  import.meta.env.VITE_API_BASE?.trim() || '/api'
