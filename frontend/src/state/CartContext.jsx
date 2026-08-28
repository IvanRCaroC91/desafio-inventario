import React, { createContext, useContext, useMemo, useState } from 'react'
import { api } from '../lib/api.js'

const CartContext = createContext(null)

export function CartProvider({ children }) {
  const [items, setItems] = useState([])
  const [checkingOut, setCheckingOut] = useState(false)
  const [checkoutError, setCheckoutError] = useState('')

  function addToCart(product) {
    setItems((prev) => {
      const existing = prev.find((i) => i.productoId === product.id)
      if (existing) {
        return prev.map((i) =>
          i.productoId === product.id ? { ...i, cantidad: i.cantidad + 1, nombre: product.nombre } : i,
        )
      }
      return [...prev, { productoId: product.id, nombre: product.nombre, cantidad: 1 }]
    })
  }

  function removeFromCart(productoId) {
    setItems((prev) => prev.filter((i) => i.productoId !== productoId))
  }

  function changeQty(productoId, cantidad) {
    setItems((prev) =>
      prev
        .map((i) => (i.productoId === productoId ? { ...i, cantidad } : i))
        .filter((i) => i.cantidad > 0),
    )
  }

  async function checkout({ usuarioId }) {
    setCheckingOut(true)
    setCheckoutError('')
    try {
      // Restricción: enviar SOLO productoId y cantidad (nunca precios/montos)
      const payload = {
        usuarioId,
        items: items.map((i) => ({ productoId: i.productoId, cantidad: i.cantidad })),
      }
      const res = await api.post('/api/ventas', payload)
      setItems([])
      return res.data
    } catch (e) {
      const msg = e?.response?.data?.message || 'No se pudo realizar la venta'
      setCheckoutError(msg)
      throw e
    } finally {
      setCheckingOut(false)
    }
  }

  const value = useMemo(
    () => ({ items, addToCart, removeFromCart, changeQty, checkout, checkingOut, checkoutError }),
    [items, checkingOut, checkoutError],
  )

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>
}

export function useCart() {
  const ctx = useContext(CartContext)
  if (!ctx) throw new Error('useCart debe usarse dentro de CartProvider')
  return ctx
}
