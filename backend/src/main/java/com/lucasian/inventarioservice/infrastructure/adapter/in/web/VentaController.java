package com.lucasian.inventarioservice.infrastructure.adapter.in.web;

import com.lucasian.inventarioservice.domain.model.Venta;
import com.lucasian.inventarioservice.domain.port.in.RealizarVentaUseCase;
import com.lucasian.inventarioservice.infrastructure.adapter.in.web.dto.CrearVentaRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final RealizarVentaUseCase realizarVentaUseCase;

    public VentaController(RealizarVentaUseCase realizarVentaUseCase) {
        this.realizarVentaUseCase = realizarVentaUseCase;
    }

    @PostMapping
    public ResponseEntity<Venta> crear(@RequestBody CrearVentaRequest request) {
        RealizarVentaUseCase.RealizarVentaCommand command = new RealizarVentaUseCase.RealizarVentaCommand(
                request.usuarioId(),
                request.items().stream()
                        .map(i -> new RealizarVentaUseCase.ItemVentaCommand(i.productoId(), i.cantidad()))
                        .toList()
        );

        Venta venta = realizarVentaUseCase.realizarVenta(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(venta);
    }
}
