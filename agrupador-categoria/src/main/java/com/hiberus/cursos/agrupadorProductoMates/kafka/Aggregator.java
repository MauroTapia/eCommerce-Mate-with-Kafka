package com.hiberus.cursos.agrupadorProductoMates.kafka;


import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaKey;
import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaValue;
import com.hiberus.cursos.enviadorproductos.avro.Producto;
import com.hiberus.cursos.enviadorproductos.avro.ProductoConImpuestoValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class Aggregator implements org.apache.kafka.streams.kstream.Aggregator<
        AgrupadorCategoriaKey, ProductoConImpuestoValue, AgrupadorCategoriaValue> {

    @Override
    public AgrupadorCategoriaValue apply(AgrupadorCategoriaKey agrupadorCategoriaKey,
                                         ProductoConImpuestoValue productoConImpuestoValue,
                                         AgrupadorCategoriaValue agrupadorCategoriaValue) {

        Map<String, List<Producto>> productosPorCategoria =
                (agrupadorCategoriaValue != null && agrupadorCategoriaValue.getProductosPorCategoria() != null)
                        ? new HashMap<>(agrupadorCategoriaValue.getProductosPorCategoria())
                        : new HashMap<>();

        List<Producto> productosActualizados = productosPorCategoria
                .getOrDefault(productoConImpuestoValue.getCategoria(), new ArrayList<>());

        boolean productoYaExiste = productosActualizados.stream()
                .anyMatch(p -> p.getIdentificador().equals(productoConImpuestoValue.getIdentificador()));

        if (!productoYaExiste) {
            productosActualizados.add(createProducto(productoConImpuestoValue));
        }

        productosPorCategoria.put(productoConImpuestoValue.getCategoria(), productosActualizados);

        return AgrupadorCategoriaValue.newBuilder()
                .setProductosPorCategoria(productosPorCategoria)
                .build();
    }

    private Producto createProducto(ProductoConImpuestoValue productoConImpuestoValue) {
        return Producto.newBuilder()
                .setIdentificador(productoConImpuestoValue.getIdentificador())
                .setProducto(productoConImpuestoValue.getProducto())
                .setPrecioConImpuesto(productoConImpuestoValue.getPrecioConImpuesto())
                .setCategoria(productoConImpuestoValue.getCategoria())
                .build();
    }
}
