import React, { createContext, useContext, useMemo, useState } from 'react'
import { api } from '../lib/api.js'

const CartContext = createContext(null)

export function CartProvider({ children }) {
  const [items, setItems] = useState([])
  const [checkingOut, setCheckingOut] = useState(false)
  const [checkoutError, setCheckoutError] = useState('')
  const [checkoutSuccess, setCheckoutSuccess] = useState(false)

  function addToCart(product, stockDisponible) {
    setItems((prev) => {
      const existing = prev.find((i) => i.productoId === product.id)
      if (existing) {
        const nuevaCantidad = existing.cantidad + 1
        if (nuevaCantidad > stockDisponible) {
          return prev
        }
        return prev.map((i) =>
          i.productoId === product.id ? { ...i, cantidad: nuevaCantidad, nombre: product.nombre, precio: product.precio, stockDisponible } : i,
        )
      }
      if (stockDisponible < 1) {
        return prev
      }
      return [...prev, { productoId: product.id, nombre: product.nombre, cantidad: 1, precio: product.precio, stockDisponible }]
    })
  }

  function removeFromCart(productoId) {
    setItems((prev) => prev.filter((i) => i.productoId !== productoId))
  }

  function changeQty(productoId, cantidad, stockDisponible) {
    if (cantidad > stockDisponible) {
      return
    }
    setItems((prev) =>
      prev
        .map((i) => (i.productoId === productoId ? { ...i, cantidad } : i))
        .filter((i) => i.cantidad > 0),
    )
  }

  async function checkout({ usuarioId }) {
    setCheckingOut(true)
    setCheckoutError('')
    setCheckoutSuccess(false)
    try {
      const payload = {
        usuarioId,
        items: items.map((i) => ({ productoId: i.productoId, cantidad: i.cantidad })),
      }
      const res = await api.post('/api/ventas', payload)
      setItems([])
      setCheckoutSuccess(true)
      return res.data
    } catch (e) {
      let msg = 'No se pudo realizar la venta'
      if (e?.response?.data?.error === 'STOCK_INSUFICIENTE') {
        msg = `Stock insuficiente. Disponible: ${e.response.data.stockDisponible}, solicitado: ${e.response.data.cantidadSolicitada}`
      } else if (e?.response?.data?.error === 'PRODUCTO_NO_ENCONTRADO') {
        msg = 'Uno de los productos no existe'
      } else if (e?.response?.data?.message) {
        msg = e.response.data.message
      }
      setCheckoutError(msg)
      throw e
    } finally {
      setCheckingOut(false)
    }
  }

  const value = useMemo(
    () => ({ items, addToCart, removeFromCart, changeQty, checkout, checkingOut, checkoutError, checkoutSuccess }),
    [items, checkingOut, checkoutError, checkoutSuccess],
  )

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>
}

export function useCart() {
  const ctx = useContext(CartContext)
  if (!ctx) throw new Error('useCart debe usarse dentro de CartProvider')
  return ctx
}
