package com.hiberus.cursos.enviadorventas.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VentasRequest {

    private String identificador;
    private int cantidad;

}
