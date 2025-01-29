package com.hiberus.cursos.enviadorpromocion.service.mapper;

import com.hiberus.cursos.enviadorpromocion.avro.PromocionKey;
import com.hiberus.cursos.enviadorpromocion.dto.PromocionDTO;

public class PromocionKeyMapper implements Mapper<PromocionKey, PromocionDTO>{

    @Override
    public PromocionKey dtoToEntity(PromocionDTO dto) {

        return PromocionKey
                .newBuilder()
                .setNombre(dto.getNombre())
                .build();
    }
}
