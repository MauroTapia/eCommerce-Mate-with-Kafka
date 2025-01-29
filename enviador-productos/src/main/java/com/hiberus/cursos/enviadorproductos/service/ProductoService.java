package com.hiberus.cursos.enviadorproductos.service;


import com.hiberus.cursos.enviadorproductos.dto.ProductoDTO;

public interface ProductoService {

    void crear(ProductoDTO productoDTO);
    void validarProducto(ProductoDTO productoDTO);
}

