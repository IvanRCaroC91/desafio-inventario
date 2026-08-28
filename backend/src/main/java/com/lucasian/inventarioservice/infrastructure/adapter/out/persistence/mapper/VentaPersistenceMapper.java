package com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.mapper;

import java.math.BigDecimal;

import com.lucasian.inventarioservice.domain.model.ItemVenta;
import com.lucasian.inventarioservice.domain.model.Venta;
import com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.entity.DetalleVentaEntity;
import com.lucasian.inventarioservice.infrastructure.adapter.out.persistence.entity.VentaEntity;

public class VentaPersistenceMapper {

    public VentaEntity toEntity(Venta venta) {
        if (venta == null) {
            return null;
        }

        VentaEntity entity = new VentaEntity();
        entity.setId(venta.getId());
        entity.setUsuarioId(venta.getUsuarioId());
        entity.setFecha(venta.getFecha());
        entity.setTotal(venta.calcularTotal());
        entity.setMoneda("USD");
        entity.setEstado("CONFIRMADA");

        for (ItemVenta item : venta.getItems()) {
            DetalleVentaEntity det = new DetalleVentaEntity();
            det.setVenta(entity);
            det.setProductoId(item.getProductoId());
            det.setCantidad(item.getCantidad());
            det.setPrecioUnitario(item.getPrecioUnitario());
            BigDecimal subtotal = item.getSubtotal();
            det.setSubtotal(subtotal);
            entity.getDetalles().add(det);
        }

        return entity;
    }

    public Venta toDomain(VentaEntity entity) {
        if (entity == null) {
            return null;
        }
        Venta venta = new Venta();
        venta.setId(entity.getId());
        venta.setUsuarioId(entity.getUsuarioId());
        venta.setFecha(entity.getFecha());

        if (entity.getDetalles() != null) {
            for (DetalleVentaEntity det : entity.getDetalles()) {
                venta.agregarItem(new ItemVenta(det.getProductoId(), det.getCantidad(), det.getPrecioUnitario()));
            }
        }

        return venta;
    }
}
