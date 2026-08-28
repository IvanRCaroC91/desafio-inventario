package com.lucasian.inventarioservice.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import com.lucasian.inventarioservice.domain.model.Producto;
import com.lucasian.inventarioservice.domain.port.out.ProductoRepositoryPort;
import com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.mapper.ProductoPersistenceMapper;
import com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.repository.ProductoJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductoPersistenceAdapter implements ProductoRepositoryPort {

    private final ProductoJpaRepository productoJpaRepository;
    private final ProductoPersistenceMapper mapper = new ProductoPersistenceMapper();

    public ProductoPersistenceAdapter(ProductoJpaRepository productoJpaRepository) {
        this.productoJpaRepository = productoJpaRepository;
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return productoJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Producto> findAll() {
        return productoJpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Producto save(Producto producto) {
        return mapper.toDomain(productoJpaRepository.save(mapper.toEntity(producto)));
    }
}
