package com.lucasian.inventarioservice.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

import com.lucasian.inventarioservice.domain.exception.StockInsuficienteException;

public class Producto {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private int stock;

    public Producto() {
    }

    public Producto(Long id, String nombre, BigDecimal precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public void disminuirStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (this.stock < cantidad) {
            throw new StockInsuficienteException(this.id, this.stock, cantidad);
        }
        this.stock -= cantidad;
    }

    public void aumentarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.stock += cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
