package com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.repository;

import com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaJpaRepository extends JpaRepository<VentaEntity, Long> {
}
