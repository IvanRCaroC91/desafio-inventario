package com.lucasian.inventarioservice.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemVenta {

    private Long productoId;
    private int cantidad;
    private BigDecimal precioUnitario;

    public ItemVenta() {
    }

    public ItemVenta(Long productoId, int cantidad, BigDecimal precioUnitario) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        if (precioUnitario == null) {
            return null;
        }
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemVenta)) return false;
        ItemVenta itemVenta = (ItemVenta) o;
        return cantidad == itemVenta.cantidad
                && Objects.equals(productoId, itemVenta.productoId)
                && Objects.equals(precioUnitario, itemVenta.precioUnitario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productoId, cantidad, precioUnitario);
    }
}
