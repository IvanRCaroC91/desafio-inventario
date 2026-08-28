package com.lucasian.inventarioservice.domain.exception;

public class ProductoNoEncontradoException extends RuntimeException {

    private final Long productoId;

    public ProductoNoEncontradoException(Long productoId) {
        super("Producto no encontrado: id=" + productoId);
        this.productoId = productoId;
    }

    public Long getProductoId() {
        return productoId;
    }
}
