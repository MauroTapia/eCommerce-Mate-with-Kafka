package com.hiberus.cursos.enviadorpromocion.service.mapper;

import com.hiberus.cursos.enviadorpromocion.avro.PromocionValue;
import com.hiberus.cursos.enviadorpromocion.dto.PromocionDTO;

public class PromocionValueMapper implements Mapper<PromocionValue, PromocionDTO> {

    @Override
    public PromocionValue dtoToEntity(PromocionDTO dto) {

        return PromocionValue.newBuilder()
                .setNombre(dto.getNombre())
                .setDescuento(dto.getDescuento())
                .setCategoria(dto.getCategoria())
                .build();
    }
}
