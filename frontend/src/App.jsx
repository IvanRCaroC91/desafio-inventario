import React, { useMemo, useState } from 'react'
import Login from './views/Login.jsx'
import ProductCatalog from './views/ProductCatalog.jsx'
import CartDrawer from './views/CartDrawer.jsx'
import { useAuth } from './state/AuthContext.jsx'

export default function App() {
  const { token, logout } = useAuth()
  const [cartOpen, setCartOpen] = useState(false)
  const [refreshKey, setRefreshKey] = useState(0)

  const handleRefreshProducts = () => {
    setRefreshKey(prev => prev + 1)
  }

  const content = useMemo(() => {
    if (!token) return <Login />
    return <ProductCatalog key={refreshKey} onOpenCart={() => setCartOpen(true)} />
  }, [token, refreshKey])

  return (
    <div style={{ fontFamily: 'system-ui, -apple-system, Segoe UI, Roboto, sans-serif' }}>
      <header style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: 16, borderBottom: '1px solid #e5e7eb' }}>
        <div style={{ fontWeight: 700 }}>Inventario</div>
        <div style={{ display: 'flex', gap: 12, alignItems: 'center' }}>
          {token ? (
            <>
              <button onClick={() => setCartOpen(true)} style={btn()}>
                Carrito
              </button>
              <button onClick={logout} style={btnSecondary()}>
                Salir
              </button>
            </>
          ) : null}
        </div>
      </header>

      <main style={{ padding: 16 }}>{content}</main>

      <CartDrawer 
        open={cartOpen} 
        onClose={() => setCartOpen(false)} 
        onRefreshProducts={handleRefreshProducts}
      />
    </div>
  )
}

function btn() {
  return {
    border: '1px solid #111827',
    background: '#111827',
    color: 'white',
    padding: '8px 12px',
    borderRadius: 8,
    cursor: 'pointer',
  }
}

function btnSecondary() {
  return {
    border: '1px solid #d1d5db',
    background: 'white',
    color: '#111827',
    padding: '8px 12px',
    borderRadius: 8,
    cursor: 'pointer',
  }
}
