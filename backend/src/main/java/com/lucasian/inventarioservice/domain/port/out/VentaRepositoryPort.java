package com.lucasian.inventarioservice.domain.port.out;

import java.util.Optional;

import com.lucasian.inventarioservice.domain.model.Venta;

public interface VentaRepositoryPort {

    Optional<Venta> findById(Long id);

    Venta save(Venta venta);
}
