import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import LoveAppChat from '../views/LoveAppChat.vue'
import ManusChat from '../views/ManusChat.vue'

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: Home },
    { path: '/love-app', name: 'love-app', component: LoveAppChat },
    { path: '/manus', name: 'manus', component: ManusChat },
  ],
})
