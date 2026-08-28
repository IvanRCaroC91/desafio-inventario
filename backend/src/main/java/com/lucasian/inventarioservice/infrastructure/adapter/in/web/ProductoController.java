package com.lucasian.inventarioservice.infrastructure.adapter.in.web;

import java.util.List;

import com.lucasian.inventarioservice.domain.port.out.ProductoRepositoryPort;
import com.lucasian.inventarioservice.infrastructure.adapter.in.web.dto.ProductoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoRepositoryPort productoRepositoryPort;

    public ProductoController(ProductoRepositoryPort productoRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
    }

    @GetMapping
    public List<ProductoResponse> listar() {
        return productoRepositoryPort.findAll().stream()
                .map(p -> new ProductoResponse(p.getId(), p.getNombre(), p.getPrecio(), p.getStock()))
                .toList();
    }
}
