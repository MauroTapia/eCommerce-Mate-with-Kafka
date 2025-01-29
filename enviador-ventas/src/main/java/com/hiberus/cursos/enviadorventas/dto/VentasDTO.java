package com.hiberus.cursos.enviadorventas.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class VentasDTO {

    private String categoria;
    private String identificador;
    private int cantidad;
    private String producto;

}
