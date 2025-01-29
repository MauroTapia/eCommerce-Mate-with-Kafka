package com.hiberus.cursos.agrupadorProductoMates.kafka;


import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaKey;
import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaValue;
import com.hiberus.cursos.enviadorproductos.avro.Producto;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionadoValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class Aggregator implements org.apache.kafka.streams.kstream.Aggregator<AgrupadorCategoriaKey, ProductoPromocionadoValue, AgrupadorCategoriaValue> {
    @Override
    public AgrupadorCategoriaValue apply(AgrupadorCategoriaKey agrupadorCategoriaKey,
                                         ProductoPromocionadoValue productoPromocionadoValue,
                                         AgrupadorCategoriaValue agrupadorCategoriaValue) {

        List<Producto> productosExistentes = agrupadorCategoriaValue != null && agrupadorCategoriaValue.getProductos() != null
                ? agrupadorCategoriaValue.getProductos()
                : new ArrayList<>();

        List<Producto> productosActualizados = productosExistentes.stream()
                .filter(c -> !productoPromocionadoValue.getProducto().equals(c.getProducto()))
                .collect(Collectors.toList());

        productosActualizados.add(createProducto(productoPromocionadoValue));

        return AgrupadorCategoriaValue.newBuilder()
                .setProductos(productosActualizados)
                .build();
    }

    private Producto createProducto(ProductoPromocionadoValue productoPromocionadoValue){
        return Producto.newBuilder()
                .setIdentificador(productoPromocionadoValue.getIdentificador())
                .setProducto(productoPromocionadoValue.getProducto())
                .setPrecioConImpuesto(productoPromocionadoValue.getPrecioConImpuesto())
                .setPrecioPromocionado(productoPromocionadoValue.getPrecioPromocionado())
                .setPromocionTimestamp(productoPromocionadoValue.getPromocionTimestamp())
                .build();
    }
}