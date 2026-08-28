package com.lucasian.inventarioservice.infrastructure.adapter.in.web;

import java.util.Map;

import com.lucasian.inventarioservice.domain.exception.ProductoNoEncontradoException;
import com.lucasian.inventarioservice.domain.exception.StockInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> handleStockInsuficiente(StockInsuficienteException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "STOCK_INSUFICIENTE",
                        "message", ex.getMessage(),
                        "productoId", ex.getProductoId(),
                        "stockDisponible", ex.getStockDisponible(),
                        "cantidadSolicitada", ex.getCantidadSolicitada()
                ));
    }

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleProductoNoEncontrado(ProductoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "PRODUCTO_NO_ENCONTRADO",
                        "message", ex.getMessage(),
                        "productoId", ex.getProductoId()
                ));
    }
}
