package com.hiberus.cursos.consultadorventas.service;

import com.hiberus.cursos.consultadorventas.model.Venta;

import java.util.List;

public interface VentasService {

    List<Venta> getVentas();

    void guardarVenta(Venta ventas);

}
