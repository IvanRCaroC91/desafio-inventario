import React, { createContext, useContext, useEffect, useMemo, useState } from 'react'
import { api, setupAuthInterceptors } from '../lib/api.js'

const AuthContext = createContext(null)

const STORAGE_KEY = 'inventario.jwt'

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem(STORAGE_KEY) || '')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    setupAuthInterceptors(
      () => token,
      () => {
        setToken('')
        localStorage.removeItem(STORAGE_KEY)
      },
    )
  }, [])

  useEffect(() => {
    if (token) localStorage.setItem(STORAGE_KEY, token)
    else localStorage.removeItem(STORAGE_KEY)
  }, [token])

  async function login(username, password) {
    setLoading(true)
    setError('')
    try {
      const res = await api.post('/api/auth/login', { username, password })
      setToken(res.data?.token || '')
    } catch (e) {
      setError('Credenciales inválidas')
      setToken('')
    } finally {
      setLoading(false)
    }
  }

  function logout() {
    setToken('')
  }

  const value = useMemo(
    () => ({ token, loading, error, login, logout }),
    [token, loading, error],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth debe usarse dentro de AuthProvider')
  return ctx
}
