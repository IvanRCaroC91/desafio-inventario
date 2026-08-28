import axios from 'axios'

const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export const api = axios.create({
  baseURL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export function setupAuthInterceptors(getToken, onUnauthorized) {
  api.interceptors.request.use((config) => {
    const token = getToken?.()
    if (token) {
      config.headers = config.headers || {}
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  })

  api.interceptors.response.use(
    (res) => res,
    (err) => {
      if (err?.response?.status === 401) {
        onUnauthorized?.()
      }
      return Promise.reject(err)
    },
  )
}
