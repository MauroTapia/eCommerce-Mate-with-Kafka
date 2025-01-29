package com.hiberus.cursos.enviadorpromocion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class PromocionDTO {

    private String nombre;
    private Double descuento;
    private String categoria;

}
