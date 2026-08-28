package com.lucasian.inventarioservice.infrastructure.adapter.in.web;

import com.lucasian.inventarioservice.infrastructure.adapter.in.web.dto.LoginRequest;
import com.lucasian.inventarioservice.infrastructure.adapter.in.web.dto.LoginResponse;
import com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.repository.UsuarioJpaRepository;
import com.lucasian.inventarioservice.infrastructure.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtService jwtService, UsuarioJpaRepository usuarioJpaRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (request == null || request.username() == null || request.password() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UsuarioEntity user = usuarioJpaRepository.findByUsername(request.username()).orElse(null);
        if (user == null || !user.isActivo()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
