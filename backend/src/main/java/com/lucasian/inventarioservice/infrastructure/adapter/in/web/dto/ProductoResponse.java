package com.lucasian.inventarioservice.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

public record ProductoResponse(Long id, String nombre, BigDecimal precio, int stock) {
}
