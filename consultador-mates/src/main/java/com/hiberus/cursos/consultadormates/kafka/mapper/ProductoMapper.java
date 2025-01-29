package com.hiberus.cursos.consultadormates.kafka.mapper;



import com.hiberus.cursos.consultadormates.model.Producto;
import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaKey;
import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaValue;


import java.util.List;
import java.util.stream.Collectors;

public class ProductoMapper implements Mapper<AgrupadorCategoriaKey, AgrupadorCategoriaValue, List<Producto>> {

    @Override
    public List<Producto> toDTO(AgrupadorCategoriaKey categoriaKey, AgrupadorCategoriaValue value) {
      String categoria = categoriaKey.getCategoria();

        return value.getProductos().stream()
                .map(productoAvro -> Producto.builder()
                        .categoria(categoria)
                        .producto(productoAvro.getProducto())
                        .identificador(productoAvro.getIdentificador())
                        .precioConImpuesto(productoAvro.getPrecioConImpuesto())
                        .precioPromocionado(productoAvro.getPrecioPromocionado())
                        .build())
                .collect(Collectors.toList());
    }
}