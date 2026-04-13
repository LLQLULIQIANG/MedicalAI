import axios from 'axios'
import { API_BASE } from '../config'

export const http = axios.create({
  baseURL: API_BASE,
  timeout: 60_000,
})
