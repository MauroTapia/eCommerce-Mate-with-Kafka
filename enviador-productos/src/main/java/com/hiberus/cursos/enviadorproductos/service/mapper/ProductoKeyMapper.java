package com.hiberus.cursos.enviadorproductos.service.mapper;

import com.hiberus.cursos.enviadorproductos.avro.ProductoKey;
import com.hiberus.cursos.enviadorproductos.dto.ProductoDTO;


public class ProductoKeyMapper implements Mapper<ProductoKey, ProductoDTO>{

    @Override
    public ProductoKey dtoToEntity(ProductoDTO dto) {

        return ProductoKey.newBuilder()
                .setCategoria(dto.getCategoria())
                .build();
    }
}
