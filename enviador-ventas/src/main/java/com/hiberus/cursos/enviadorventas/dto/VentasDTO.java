package com.hiberus.cursos.enviadorventas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VentasDTO {

    private String identificador;
    private String identificadorVenta;
    private int cantidad;

}
