package com.hiberus.cursos.enviadorventas.service.mapper;

import com.hiberus.cursos.enviadorventas.avro.VentasValue;
import com.hiberus.cursos.enviadorventas.dto.VentasDTO;


public class VentaValueMapper implements Mapper<VentasValue, VentasDTO> {

    @Override
    public VentasValue dtoToEntity(VentasDTO dto) {

        return VentasValue.newBuilder()
                .setIdentificador(dto.getIdentificador())
                .setCantidad(dto.getCantidad())
                .setProducto(dto.getProducto())
                .setVentaTimestamp(System.currentTimeMillis())
                .build();
    }
}
