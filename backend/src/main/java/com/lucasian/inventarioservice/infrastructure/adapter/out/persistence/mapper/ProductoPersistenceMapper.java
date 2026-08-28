package com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.mapper;

import com.lucasian.inventarioservice.domain.model.Producto;
import com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.entity.ProductoEntity;

import java.time.Instant;

public class ProductoPersistenceMapper {

    public Producto toDomain(ProductoEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Producto(entity.getId(), entity.getNombre(), entity.getPrecio(), entity.getStock());
    }

    public ProductoEntity toEntity(Producto domain) {
        if (domain == null) {
            return null;
        }
        ProductoEntity entity = new ProductoEntity();
        entity.setId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setPrecio(domain.getPrecio());
        entity.setStock(domain.getStock());
        entity.setActivo(true);
        entity.setCreatedAt(Instant.now());
        return entity;
    }
}
