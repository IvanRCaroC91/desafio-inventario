package com.lucasian.inventarioservice.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.lucasian.inventarioservice.domain.model.Producto;

public interface ProductoRepositoryPort {

    Optional<Producto> findById(Long id);

    List<Producto> findAll();

    Producto save(Producto producto);
}
