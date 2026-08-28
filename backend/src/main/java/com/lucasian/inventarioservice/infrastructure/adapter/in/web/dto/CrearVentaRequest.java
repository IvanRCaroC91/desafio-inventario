package com.lucasian.inventarioservice.infrastructure.adapter.in.web.dto;

import java.util.List;

public record CrearVentaRequest(Long usuarioId, List<ItemVentaRequest> items) {

    public record ItemVentaRequest(Long productoId, int cantidad) {
    }
}
