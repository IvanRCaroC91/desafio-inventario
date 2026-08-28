package com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.repository;

import com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoJpaRepository extends JpaRepository<ProductoEntity, Long> {
}
