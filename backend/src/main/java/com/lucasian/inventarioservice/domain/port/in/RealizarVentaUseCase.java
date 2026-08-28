package com.lucasian.inventarioservice.domain.port.in;

import java.util.List;

import com.lucasian.inventarioservice.domain.model.Venta;

public interface RealizarVentaUseCase {

    Venta realizarVenta(RealizarVentaCommand command);

    record RealizarVentaCommand(Long usuarioId, List<ItemVentaCommand> items) {
        public RealizarVentaCommand {
            if (usuarioId == null) {
                throw new IllegalArgumentException("usuarioId no puede ser null");
            }
            if (items == null || items.isEmpty()) {
                throw new IllegalArgumentException("items no puede ser null o vacío");
            }
        }
    }

    record ItemVentaCommand(Long productoId, int cantidad) {
        public ItemVentaCommand {
            if (productoId == null) {
                throw new IllegalArgumentException("productoId no puede ser null");
            }
            if (cantidad <= 0) {
                throw new IllegalArgumentException("cantidad debe ser mayor a 0");
            }
        }
    }
}
