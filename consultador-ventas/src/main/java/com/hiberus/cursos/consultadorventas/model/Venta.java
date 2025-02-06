package com.hiberus.cursos.consultadorventas.model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Venta {

    private String identificador;
    @Id
    private String identificadorVenta;
    private int cantidad;
    private double precioConImpuesto;
    private double precioPromocionado;
}