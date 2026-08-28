package com.lucasian.inventarioservice.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Venta {

    private Long id;
    private Long usuarioId;
    private Instant fecha;
    private final List<ItemVenta> items = new ArrayList<>();

    public Venta() {
    }

    public Venta(Long id, Long usuarioId, Instant fecha, List<ItemVenta> items) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        if (items != null) {
            this.items.addAll(items);
        }
    }

    public void agregarItem(ItemVenta item) {
        if (item == null) {
            throw new IllegalArgumentException("El item no puede ser null");
        }
        this.items.add(item);
    }

    public BigDecimal calcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemVenta item : items) {
            BigDecimal subtotal = item.getSubtotal();
            if (subtotal == null) {
                throw new IllegalStateException("No se puede calcular el total: precioUnitario es null");
            }
            total = total.add(subtotal);
        }
        return total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public List<ItemVenta> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void setItems(List<ItemVenta> nuevosItems) {
        this.items.clear();
        if (nuevosItems != null) {
            this.items.addAll(nuevosItems);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venta)) return false;
        Venta venta = (Venta) o;
        return Objects.equals(id, venta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
