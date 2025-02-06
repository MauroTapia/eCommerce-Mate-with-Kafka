package com.hiberus.cursos.enviadorventas.service;


import com.hiberus.cursos.enviadorventas.model.VentasRequest;

public interface VentasService {

    void crear(VentasRequest ventasRequest);
    void validarVentasDTO(VentasRequest ventasRequest);
}
