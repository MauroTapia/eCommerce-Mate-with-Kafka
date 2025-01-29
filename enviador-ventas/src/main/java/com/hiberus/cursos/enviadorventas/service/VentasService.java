package com.hiberus.cursos.enviadorventas.service;


import com.hiberus.cursos.enviadorventas.dto.VentasDTO;

public interface VentasService {

    void crear(VentasDTO ventasDTO);
    void validarVentasDTO(VentasDTO ventasDTO);
}
