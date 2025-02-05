package com.hiberus.cursos.aplicadorventas.kafka;

import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionado;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionadoKey;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionadoValue;
import com.hiberus.cursos.enviadorventas.avro.VentasKey;
import com.hiberus.cursos.enviadorventas.avro.VentasValue;
import com.hiberus.cursos.mix.avro.VentasProductosMateKey;
import com.hiberus.cursos.mix.avro.VentasProductosMateValue;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;


@SpringBootApplication
@Slf4j
public class KStreamsVentaExample {

    public static void main(final String[] args) {
        SpringApplication.run(KStreamsVentaExample.class, args);
    }



    @Bean
    public BiFunction<KStream<ProductoPromocionadoKey, ProductoPromocionadoValue>,
            KStream<VentasKey, VentasValue>,
            KStream<VentasProductosMateKey, VentasProductosMateValue>> joiner() {

        SpecificAvroSerde<ProductoPromocionadoValue> productoSerde = new SpecificAvroSerde<>();
        productoSerde.configure(Collections.singletonMap("schema.registry.url", "http://schema-registry:8081"), false);

        SpecificAvroSerde<VentasValue> ventasSerde = new SpecificAvroSerde<>();
        ventasSerde.configure(Collections.singletonMap("schema.registry.url", "http://schema-registry:8081"), false);

        return (productosStream, ventasStream) -> {

            KStream<String, ProductoPromocionadoValue> productosStreamSerialized = productosStream
                    .map((key, value) -> {
                        String productoKey = value.getProductosPorCategoria().values().stream()
                                .flatMap(List::stream)
                                .map(ProductoPromocionado::getIdentificador)
                                .findFirst()
                                .orElse("SIN_IDENTIFICADOR");
                        return new KeyValue<>(productoKey, value);
                    });

            KTable<String, ProductoPromocionadoValue> productosKTable = productosStreamSerialized
                    .toTable(Named.as("PRODUCTOS_PROMOCIONADOS"), Materialized.<String, ProductoPromocionadoValue, KeyValueStore<Bytes, byte[]>>as("PRODUCTOS_PROMOCIONADOS_TABLE")
                            .withKeySerde(Serdes.String())
                            .withValueSerde(productoSerde));

            KTable<String, VentasValue> ventasKTable = ventasStream
                    .selectKey((k, v) -> v.getIdentificador())
                    .toTable(Named.as("VENTAS"), Materialized.<String, VentasValue, KeyValueStore<Bytes, byte[]>>as("VENTAS_TABLE")
                            .withKeySerde(Serdes.String())
                            .withValueSerde(ventasSerde));

            // 4. Realizamos el JOIN entre productos y ventas por el identificador
            return productosKTable.join(ventasKTable, (productoPromocionadoValue, ventaValue) -> {
                        ProductoPromocionado matchingProducto = productoPromocionadoValue.getProductosPorCategoria()
                                .values()
                                .stream()
                                .flatMap(List::stream)
                                .filter(producto -> producto.getIdentificador().equals(ventaValue.getIdentificador()))
                                .findFirst()
                                .orElse(null);

                        if (matchingProducto != null) {
                            log.info("Producto encontrado: {}", matchingProducto.getProducto());
                            return VentasProductosMateValue.newBuilder()
                                    .setIdentificador(matchingProducto.getIdentificador())
                                    .setIdentificadorVenta(ventaValue.getIdentificadorVenta())
                                    .setCantidad(ventaValue.getCantidad())
                                    .build();
                        } else {
                            log.warn("No se encontró un producto coincidente para ventaValue: {}", ventaValue);
                            return VentasProductosMateValue.newBuilder()
                                    .setIdentificador("SIN_COINCIDENCIA")
                                    .setIdentificadorVenta(ventaValue.getIdentificadorVenta())
                                    .setCantidad(0)
                                    .build();
                        }
                    })
                    .toStream()
                    .filter((k, v) -> !"SIN_COINCIDENCIA".equals(v.getIdentificador())) // Excluye las ventas sin coincidencia
                    .peek((k, v) -> log.info("Producto con promoción creada -> clave: {} valor: {}", k, v))
                    .selectKey((k, v) -> VentasProductosMateKey.newBuilder()
                            .setIdentificadorVenta(v.getIdentificadorVenta())
                            .build());
        };
    }
}