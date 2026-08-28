import React, { useState } from 'react'
import { useAuth } from '../state/AuthContext.jsx'

export default function Login() {
  const { login, loading, error } = useAuth()
  const [username, setUsername] = useState('admin')
  const [password, setPassword] = useState('')

  async function onSubmit(e) {
    e.preventDefault()
    await login(username, password)
  }

  return (
    <div style={{ maxWidth: 420, margin: '40px auto', border: '1px solid #e5e7eb', borderRadius: 12, padding: 16 }}>
      <h2 style={{ marginTop: 0 }}>Iniciar sesión</h2>

      <form onSubmit={onSubmit} style={{ display: 'grid', gap: 12 }}>
        <label style={label()}>
          Usuario
          <input value={username} onChange={(e) => setUsername(e.target.value)} style={input()} />
        </label>

        <label style={label()}>
          Password
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} style={input()} />
        </label>

        {error ? <div style={{ color: '#b91c1c' }}>{error}</div> : null}

        <button disabled={loading} style={btn()}>
          {loading ? 'Ingresando...' : 'Login'}
        </button>

        <div style={{ fontSize: 12, color: '#6b7280' }}>
          Nota: el backend valida contra la tabla <code>usuarios</code> (BCrypt).
        </div>
      </form>
    </div>
  )
}

function label() {
  return { display: 'grid', gap: 6, fontSize: 14, color: '#111827' }
}

function input() {
  return { border: '1px solid #d1d5db', borderRadius: 8, padding: '10px 12px', fontSize: 14 }
}

function btn() {
  return { border: '1px solid #111827', background: '#111827', color: 'white', padding: '10px 12px', borderRadius: 8, cursor: 'pointer' }
}
