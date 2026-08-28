package com.lucasian.inventarioservice.domain.exception;

public class StockInsuficienteException extends RuntimeException {

    private final Long productoId;
    private final int stockDisponible;
    private final int cantidadSolicitada;

    public StockInsuficienteException(Long productoId, int stockDisponible, int cantidadSolicitada) {
        super("Stock insuficiente para el producto id=" + productoId
                + ". Stock disponible=" + stockDisponible
                + ", cantidad solicitada=" + cantidadSolicitada);
        this.productoId = productoId;
        this.stockDisponible = stockDisponible;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public Long getProductoId() {
        return productoId;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }
}
