package com.hiberus.cursos.agrupadorProductoMates.kafka;

import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaKey;
import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaValue;
import com.hiberus.cursos.enviadorproductos.avro.ProductoConImpuestoValue;
import com.hiberus.cursos.enviadorproductos.avro.ProductoKey;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@Slf4j
public class ProductosAggregator {

    @Autowired
    Aggregator aggregator;

    @Autowired
    Initializer initializer;

    @Bean
    public Function<KStream<ProductoKey, ProductoConImpuestoValue>, KStream<AgrupadorCategoriaKey, ProductoConImpuestoValue>> aggregateProductos() {
        return productosStream -> productosStream
                .peek((k, v) -> log.info("[aggregateProductos] Producto recibido -> clave: {}, valor: {}", k, v))
                .filter((k, v) -> v != null && v.getCategoria() != null)
                .selectKey((k, v) -> AgrupadorCategoriaKey.newBuilder().setCategoria(v.getCategoria()).build())
                .groupByKey()
                .aggregate(
                        initializer,
                        aggregator,
                        Named.as("PRODUCTOS_POR_CATEGORIA"),
                        Materialized.<AgrupadorCategoriaKey, AgrupadorCategoriaValue, KeyValueStore<Bytes, byte[]>>as("PRODUCTOS_POR_CATEGORIA")
                )
                .toStream()
                .peek((k, v) -> log.info("[aggregateProductos] Productos agrupados -> clave: {}, valor: {}", k, v));
    }
}