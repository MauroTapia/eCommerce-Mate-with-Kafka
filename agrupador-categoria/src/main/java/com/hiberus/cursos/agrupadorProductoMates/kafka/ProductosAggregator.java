package com.hiberus.cursos.agrupadorProductoMates.kafka;

import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaKey;
import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaValue;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionadoKey;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionadoValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
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
    public Function<KStream<ProductoPromocionadoKey, ProductoPromocionadoValue>, KStream<AgrupadorCategoriaKey, ProductoPromocionadoValue>> aggregateProductos() {
        return profesoresStream -> profesoresStream
                .peek((k, v) -> log.info("[aggregateProductos] Recibido profesor -> clave: {}, valor: {}", k, v))
                .selectKey((k, v) -> AgrupadorCategoriaKey.newBuilder().setCategoria(v.getCategoria()).build())
                .groupByKey()
                .aggregate(initializer, aggregator, Named.as("PRODUCTOS_POR_CATEGORIA"), Materialized.as("PRODUCTOS_POR_CATEGORIA"))
                .toStream()
                .peek((k, v) -> log.info("[aggregateProductos] Productos agrupados -> clase: {}, valor: {}", k, v));
    }
}