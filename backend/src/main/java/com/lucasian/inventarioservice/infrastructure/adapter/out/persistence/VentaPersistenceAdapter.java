package com.lucasian.inventarioservice.infrastructure.adapter.out.persistence;

import java.util.Optional;

import com.lucasian.inventarioservice.domain.model.Venta;
import com.lucasian.inventarioservice.domain.port.out.VentaRepositoryPort;
import com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.mapper.VentaPersistenceMapper;
import com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.repository.VentaJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class VentaPersistenceAdapter implements VentaRepositoryPort {

    private final VentaJpaRepository ventaJpaRepository;
    private final VentaPersistenceMapper mapper = new VentaPersistenceMapper();

    public VentaPersistenceAdapter(VentaJpaRepository ventaJpaRepository) {
        this.ventaJpaRepository = ventaJpaRepository;
    }

    @Override
    public Optional<Venta> findById(Long id) {
        return ventaJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Venta save(Venta venta) {
        return mapper.toDomain(ventaJpaRepository.save(mapper.toEntity(venta)));
    }
}
