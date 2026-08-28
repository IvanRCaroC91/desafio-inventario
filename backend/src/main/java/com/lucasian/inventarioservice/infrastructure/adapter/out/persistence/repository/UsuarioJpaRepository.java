package com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.repository;

import java.util.Optional;

import com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByUsername(String username);
}
