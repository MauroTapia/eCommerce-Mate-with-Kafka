package com.hiberus.cursos.enviadorproductos.service.mapper;


import com.hiberus.cursos.enviadorproductos.avro.ProductoValue;
import com.hiberus.cursos.enviadorproductos.dto.ProductoDTO;



public class ProductoValueMapper implements Mapper<ProductoValue, ProductoDTO> {

    @Override
    public ProductoValue dtoToEntity(ProductoDTO dto) {

        return ProductoValue.newBuilder()
                .setIdentificador(dto.getIdentificador())
                .setProducto(dto.getProducto())
                .setPrecio(dto.getPrecio())
                .setCategoria(dto.getCategoria())
                .build();
    }
}
