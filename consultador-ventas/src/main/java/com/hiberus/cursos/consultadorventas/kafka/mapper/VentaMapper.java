package com.hiberus.cursos.consultadorventas.kafka.mapper;



import com.hiberus.cursos.consultadorventas.model.Venta;
import com.hiberus.cursos.mix.avro.VentasProductosMateKey;
import com.hiberus.cursos.mix.avro.VentasProductosMateValue;

import java.util.List;


public class VentaMapper implements Mapper<VentasProductosMateKey, VentasProductosMateValue, Venta> {

    @Override
    public List<Venta> toDTO(VentasProductosMateKey key, VentasProductosMateValue value) {
        return List.of(
                Venta.builder()
                        .identificadorVenta(key.getIdentificadorVenta())
                        .identificador(value.getIdentificador())
                        .cantidad(value.getCantidad())
                        .precioConImpuesto(value.getPrecioConImpuesto())
                        .precioPromocionado(value.getPrecioPromocionado())
                        .build()
        );
    }
}