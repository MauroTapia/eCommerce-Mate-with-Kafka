package com.hiberus.cursos.aplicadorpromociones.kafka;


import com.hiberus.cursos.enviadorproductos.avro.*;
import com.hiberus.cursos.enviadorpromocion.avro.PromocionKey;
import com.hiberus.cursos.enviadorpromocion.avro.PromocionValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@SpringBootApplication
@Slf4j
public class KStreamsPromocionExample {

    public static void main(final String[] args) {
        SpringApplication.run(KStreamsPromocionExample.class, args);
    }

    @Bean
    public BiFunction<KStream<AgrupadorCategoriaKey, AgrupadorCategoriaValue>,
            KStream<PromocionKey, PromocionValue>,
            KStream<ProductoPromocionadoKey, ProductoPromocionadoValue>> joiner() {

        return (productosStream, promocionesStream) -> {

            KTable<ProductoPromocionadoKey, PromocionValue> promocionKTable = promocionesStream
                    .selectKey((k, v) -> ProductoPromocionadoKey.newBuilder()
                            .setCategoria(v.getCategoria())
                            .build())
                    .toTable(Named.as("PROMOCIONES"), Materialized.as("PROMOCIONES_TABLE"));

            KTable<ProductoPromocionadoKey, AgrupadorCategoriaValue> productoKTable = productosStream
                    .selectKey((k, v) -> ProductoPromocionadoKey.newBuilder()
                            .setCategoria(k.getCategoria())
                            .build())
                    .toTable(Named.as("PRODUCTOS-TRANSFORMADOS"), Materialized.as("PRODUCTOS_TRANSFORMADOS_TABLE"));

            return productoKTable
                    .outerJoin(promocionKTable, (productoValue, promocionValue) -> {
                        if (productoValue == null) {
                            return null;
                        }

                        Map<String, List<ProductoPromocionado>> productosPorCategoria = new HashMap<>();

                        productoValue.getProductosPorCategoria().forEach((categoria, productos) -> {
                            List<ProductoPromocionado> productosPromocionados = new ArrayList<>();

                            for (Producto producto : productos) {
                                double precioPromocionado = calcularPrecioPromocionado(producto, promocionValue);

                                ProductoPromocionado productoPromocionado = ProductoPromocionado.newBuilder()
                                        .setCategoria(producto.getCategoria())
                                        .setIdentificador(producto.getIdentificador())
                                        .setProducto(producto.getProducto())
                                        .setPrecioConImpuesto(producto.getPrecioConImpuesto())
                                        .setPrecioPromocionado(precioPromocionado)
                                        .setPromocionTimestamp(System.currentTimeMillis())
                                        .build();

                                productosPromocionados.add(productoPromocionado);
                            }

                            productosPorCategoria.put(categoria, productosPromocionados);
                        });

                        return ProductoPromocionadoValue.newBuilder()
                                .setProductosPorCategoria(productosPorCategoria != null ? productosPorCategoria : new HashMap<>())
                                .build();
                    })
                    .toStream()
                    .map((key, value) -> KeyValue.pair(
                            ProductoPromocionadoKey.newBuilder().setCategoria(key.getCategoria()).build(),
                            value
                    ))
                    .peek((k, v) -> log.info("Promociones aplicadas -> clave: {} valor: {}", k, v));
        };
    }

    private double calcularPrecioPromocionado(Producto producto, PromocionValue promocionValue) {
        if (promocionValue != null && promocionValue.getDescuento() > 0) {
            double descuento = promocionValue.getDescuento();
            return redondear(producto.getPrecioConImpuesto() * (1 - descuento / 100));
        }
        return redondear(producto.getPrecioConImpuesto());
    }

    private double redondear(double valor) {
        BigDecimal bd = new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
