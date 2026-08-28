import React, { useMemo, useState } from 'react'
import { useCart } from '../state/CartContext.jsx'
import { useAuth } from '../state/AuthContext.jsx'

export default function CartDrawer({ open, onClose, onRefreshProducts }) {
  const { items, removeFromCart, changeQty, checkout, checkingOut, checkoutError, checkoutSuccess } = useCart()
  const { token } = useAuth()
  const [usuarioId, setUsuarioId] = useState(1)

  const total = useMemo(() => {
    return items.reduce((sum, item) => {
      const subtotal = (item.precio || 0) * item.cantidad
      return sum + subtotal
    }, 0)
  }, [items])

  const content = useMemo(() => {
    if (!open) return null

    return (
      <div style={overlay()} onClick={onClose}>
        <div style={drawer()} onClick={(e) => e.stopPropagation()}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <h3 style={{ margin: 0 }}>Carrito</h3>
            <button onClick={onClose} style={btnSecondary()}>
              Cerrar
            </button>
          </div>

          <div style={{ marginTop: 12, display: 'grid', gap: 10 }}>
            {!token ? (
              <div style={{ color: '#b91c1c', padding: 12, background: '#fee2e2', borderRadius: 8 }}>
                Debes iniciar sesión para realizar compras
              </div>
            ) : (
              <>
                {items.length === 0 ? (
                  <div style={{ color: '#6b7280' }}>No hay items en el carrito.</div>
                ) : (
                  <>
                    {items.map((i) => (
                      <div key={i.productoId} style={{ border: '1px solid #e5e7eb', borderRadius: 10, padding: 10 }}>
                        <div style={{ fontWeight: 600 }}>{i.nombre || `Producto ${i.productoId}`}</div>
                        <div style={{ fontSize: 13, color: '#374151', marginTop: 4 }}>
                          ${i.precio || 0} × {i.cantidad} = <b>${((i.precio || 0) * i.cantidad).toFixed(2)}</b>
                        </div>
                        <div style={{ display: 'flex', gap: 8, alignItems: 'center', marginTop: 8 }}>
                          <button 
                            onClick={() => changeQty(i.productoId, i.cantidad - 1, i.stockDisponible || 100)} 
                            style={btnSecondary()}
                          >
                            -
                          </button>
                          <div style={{ minWidth: 28, textAlign: 'center' }}>{i.cantidad}</div>
                          <button 
                            onClick={() => changeQty(i.productoId, i.cantidad + 1, i.stockDisponible || 100)} 
                            style={btnSecondary()}
                          >
                            +
                          </button>
                          <button onClick={() => removeFromCart(i.productoId)} style={btnDanger()}>
                            Quitar
                          </button>
                        </div>
                      </div>
                    ))}

                    <div style={{ borderTop: '1px solid #e5e7eb', paddingTop: 10, marginTop: 10 }}>
                      <div style={{ fontSize: 16, fontWeight: 700, textAlign: 'right' }}>
                        TOTAL: ${total.toFixed(2)}
                      </div>
                    </div>
                  </>
                )}

                {checkoutSuccess ? (
                  <div style={{ color: '#065f46', padding: 12, background: '#d1fae5', borderRadius: 8 }}>
                    ¡Venta realizada exitosamente! El inventario ha sido actualizado.
                  </div>
                ) : null}

                {checkoutError ? <div style={{ color: '#b91c1c' }}>{checkoutError}</div> : null}

                <button
                  disabled={items.length === 0 || checkingOut || !token}
                  style={items.length === 0 || checkingOut || !token ? btnDisabled() : btn()}
                  onClick={async () => {
                    await checkout({ usuarioId })
                    onRefreshProducts?.()
                  }}
                >
                  {checkingOut ? 'Procesando...' : 'Checkout'}
                </button>
              </>
            )}
          </div>
        </div>
      </div>
    )
  }, [open, onClose, items, removeFromCart, changeQty, checkout, checkingOut, checkoutError, checkoutSuccess, usuarioId, token, total, onRefreshProducts])

  return content
}

function overlay() {
  return {
    position: 'fixed',
    inset: 0,
    background: 'rgba(0,0,0,0.35)',
    display: 'flex',
    justifyContent: 'flex-end',
  }
}

function drawer() {
  return {
    width: 380,
    maxWidth: '100%',
    height: '100%',
    background: 'white',
    padding: 16,
    boxShadow: '0 10px 30px rgba(0,0,0,0.2)',
  }
}

function input() {
  return { border: '1px solid #d1d5db', borderRadius: 8, padding: '10px 12px', fontSize: 14 }
}

function btn() {
  return { border: '1px solid #111827', background: '#111827', color: 'white', padding: '10px 12px', borderRadius: 8, cursor: 'pointer' }
}

function btnDisabled() {
  return { border: '1px solid #d1d5db', background: '#f3f4f6', color: '#6b7280', padding: '10px 12px', borderRadius: 8, cursor: 'not-allowed' }
}

function btnSecondary() {
  return { border: '1px solid #d1d5db', background: 'white', color: '#111827', padding: '8px 12px', borderRadius: 8, cursor: 'pointer' }
}

function btnDanger() {
  return { border: '1px solid #fecaca', background: '#fee2e2', color: '#991b1b', padding: '8px 12px', borderRadius: 8, cursor: 'pointer' }
}
