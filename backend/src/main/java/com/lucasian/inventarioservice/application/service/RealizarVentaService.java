package com.lucasian.inventarioservice.application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucasian.inventarioservice.domain.exception.ProductoNoEncontradoException;
import com.lucasian.inventarioservice.domain.model.ItemVenta;
import com.lucasian.inventarioservice.domain.model.Producto;
import com.lucasian.inventarioservice.domain.model.Venta;
import com.lucasian.inventarioservice.domain.port.in.RealizarVentaUseCase;
import com.lucasian.inventarioservice.domain.port.out.ProductoRepositoryPort;
import com.lucasian.inventarioservice.domain.port.out.VentaRepositoryPort;

@Service
public class RealizarVentaService implements RealizarVentaUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;
    private final VentaRepositoryPort ventaRepositoryPort;

    public RealizarVentaService(ProductoRepositoryPort productoRepositoryPort, VentaRepositoryPort ventaRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
        this.ventaRepositoryPort = ventaRepositoryPort;
    }

    @Override
    @Transactional
    public Venta realizarVenta(RealizarVentaCommand command) {
        Venta venta = new Venta();
        venta.setUsuarioId(command.usuarioId());
        venta.setFecha(Instant.now());

        for (ItemVentaCommand itemCmd : command.items()) {
            Producto producto = productoRepositoryPort.findById(itemCmd.productoId())
                    .orElseThrow(() -> new ProductoNoEncontradoException(itemCmd.productoId()));

            producto.disminuirStock(itemCmd.cantidad());

            ItemVenta item = new ItemVenta(producto.getId(), itemCmd.cantidad(), producto.getPrecio());
            venta.agregarItem(item);

            productoRepositoryPort.save(producto);
        }

        venta.calcularTotal();

        return ventaRepositoryPort.save(venta);
    }
}
