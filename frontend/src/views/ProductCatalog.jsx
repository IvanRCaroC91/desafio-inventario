import React, { useEffect, useMemo, useState } from 'react'
import { api } from '../lib/api.js'
import { useCart } from '../state/CartContext.jsx'

export default function ProductCatalog({ onOpenCart }) {
  const { addToCart } = useCart()
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  async function fetchProducts() {
    setLoading(true)
    setError('')
    try {
      const res = await api.get('/api/productos')
      setProducts(res.data || [])
    } catch (e) {
      setError('No se pudieron cargar los productos')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchProducts()
    const id = setInterval(fetchProducts, 5000) // stock en vivo
    return () => clearInterval(id)
  }, [])

  const body = useMemo(() => {
    if (loading && products.length === 0) return <div>Cargando...</div>
    if (error) return <div style={{ color: '#b91c1c' }}>{error}</div>

    return (
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: 12 }}>
        {products.map((p) => (
          <div key={p.id} style={{ border: '1px solid #e5e7eb', borderRadius: 12, padding: 12 }}>
            <div style={{ fontWeight: 700 }}>{p.nombre}</div>
            <div style={{ fontSize: 14, color: '#374151', marginTop: 6 }}>
              Precio: <b>${p.precio}</b>
            </div>
            <div style={{ fontSize: 14, color: p.stock > 0 ? '#065f46' : '#b91c1c', marginTop: 6 }}>
              Stock: <b>{p.stock}</b>
            </div>

            <div style={{ display: 'flex', gap: 8, marginTop: 12 }}>
              <button
                onClick={() => {
                  addToCart(p, p.stock)
                  onOpenCart?.()
                }}
                disabled={p.stock <= 0}
                style={p.stock > 0 ? btn() : btnDisabled()}
              >
                Agregar
              </button>
              <button onClick={fetchProducts} style={btnSecondary()}>
                Refresh
              </button>
            </div>
          </div>
        ))}
      </div>
    )
  }, [loading, products, error, addToCart, onOpenCart])

  return (
    <div style={{ display: 'grid', gap: 12 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h2 style={{ margin: 0 }}>Catálogo</h2>
        <button onClick={onOpenCart} style={btn()}>
          Ver carrito
        </button>
      </div>
      {body}
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

function btnDisabled() {
  return {
    border: '1px solid #d1d5db',
    background: '#f3f4f6',
    color: '#6b7280',
    padding: '8px 12px',
    borderRadius: 8,
    cursor: 'not-allowed',
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
