package com.hiberus.cursos.consultadormates.model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Producto {
    private String categoria;
    @Id
    private String identificador;
    private String producto;
    private Double precioConImpuesto;
    private Double precioPromocionado;
}