package com.hiberus.cursos.enviadorventas.service.mapper;


import com.hiberus.cursos.enviadorventas.avro.VentasKey;
import com.hiberus.cursos.enviadorventas.dto.VentasDTO;

public class VentaKeyMapper implements Mapper<VentasKey, VentasDTO>{

    @Override
    public VentasKey dtoToEntity(VentasDTO dto) {

        return VentasKey.newBuilder()
                .setIdentificadorVenta(dto.getIdentificadorVenta())
                .build();
    }
}
