package com.hiberus.cursos.enviadorventas.service.mapper;


import com.hiberus.cursos.enviadorventas.avro.VentasKey;
import com.hiberus.cursos.enviadorventas.model.dto.VentasDTO;

import java.util.UUID;

public class VentaKeyMapper implements Mapper<VentasKey, VentasDTO> {
    @Override
    public VentasKey dtoToEntity(VentasDTO dto) {
        String idVenta = UUID.randomUUID().toString();
        dto.setIdentificadorVenta(idVenta);

        return VentasKey.newBuilder()
                .setIdentificadorVenta(idVenta)
                .build();
    }
}