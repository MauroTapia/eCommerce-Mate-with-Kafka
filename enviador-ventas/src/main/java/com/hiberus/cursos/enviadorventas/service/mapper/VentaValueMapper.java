package com.hiberus.cursos.enviadorventas.service.mapper;

import com.hiberus.cursos.enviadorventas.avro.VentasValue;
import com.hiberus.cursos.enviadorventas.model.dto.VentasDTO;


public class VentaValueMapper implements Mapper<VentasValue, VentasDTO> {

    @Override
    public VentasValue dtoToEntity(VentasDTO dto) {

        return VentasValue.newBuilder()
                .setIdentificadorVenta(dto.getIdentificadorVenta())
                .setIdentificador(dto.getIdentificador())
                .setCantidad(dto.getCantidad())
                .setVentaTimestamp(System.currentTimeMillis())
                .build();
    }
}
