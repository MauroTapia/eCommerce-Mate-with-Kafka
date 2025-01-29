package com.hiberus.cursos.enviadorpromocion.service;


import com.hiberus.cursos.enviadorpromocion.dto.PromocionDTO;

public interface PromocionService {

    void crear(PromocionDTO promocionDTO);
    void validarPromocionDTO(PromocionDTO promocionDTO);
}
