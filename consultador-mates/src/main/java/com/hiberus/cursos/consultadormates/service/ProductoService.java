package com.hiberus.cursos.consultadormates.service;

import com.hiberus.cursos.consultadormates.model.Producto;

import java.util.List;

public interface ProductoService {

    List<Producto> getProductos();

    void guardarProductoMates(Producto productoM);
}
