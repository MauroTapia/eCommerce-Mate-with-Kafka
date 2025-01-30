package com.hiberus.cursos.consultadormates.kafka.mapper;
import com.hiberus.cursos.consultadormates.model.Producto;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionado;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionadoKey;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionadoValue;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductoMapper implements Mapper<ProductoPromocionadoKey, ProductoPromocionadoValue, List<Producto>> {

    @Override
    public List<Producto> toDTO(ProductoPromocionadoKey categoriaKey, ProductoPromocionadoValue value) {
        Map<String, List<ProductoPromocionado>> productosPorCategoria = value.getProductosPorCategoria();

        return productosPorCategoria.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .map(productoAvro -> Producto.builder()
                        .categoria(categoriaKey.getCategoria())
                        .producto(productoAvro.getProducto())
                        .identificador(productoAvro.getIdentificador())
                        .precioConImpuesto(productoAvro.getPrecioConImpuesto())
                        .precioPromocionado(productoAvro.getPrecioPromocionado())
                        .promocionTimestamp(productoAvro.getPromocionTimestamp())
                        .build())
                .collect(Collectors.toList());
    }
}