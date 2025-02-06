package com.hiberus.cursos.enviadorventas.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class VentasDTO {

    private String identificador;
    private int cantidad;
    private String identificadorVenta;

}
