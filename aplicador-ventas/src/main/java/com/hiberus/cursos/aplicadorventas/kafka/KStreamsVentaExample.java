package com.hiberus.cursos.aplicadorventas.kafka;



import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaKey;
import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaValue;
import com.hiberus.cursos.enviadorproductos.avro.Producto;
import com.hiberus.cursos.enviadorventas.avro.VentasKey;
import com.hiberus.cursos.enviadorventas.avro.VentasValue;
import com.hiberus.cursos.mix.avro.VentasProductosMateKey;
import com.hiberus.cursos.mix.avro.VentasProductosMateValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;
import java.util.function.BiFunction;


@SpringBootApplication
@Slf4j
public class KStreamsVentaExample {

    public static void main(final String[] args) {
        SpringApplication.run(KStreamsVentaExample.class, args);
    }

    @Bean
    public BiFunction<KStream<AgrupadorCategoriaKey, AgrupadorCategoriaValue>,
            KStream<VentasKey, VentasValue>,
            KStream<VentasProductosMateKey, VentasProductosMateValue>> joiner() {
        return (agrupadorStream, ventasStream) -> {

            KTable<VentasProductosMateKey, VentasValue> ventaKTable = ventasStream
                    .selectKey((k, v) -> VentasProductosMateKey.newBuilder()
                            .setCategoria(k.getCategoria())
                            .build())
                    .toTable(Named.as("VENTAS"), Materialized.as("VENTAS_TABLE"));

            KTable<VentasProductosMateKey, AgrupadorCategoriaValue> agrupadorKTable = agrupadorStream
                    .selectKey((k, v) -> VentasProductosMateKey.newBuilder()
                            .setCategoria(k.getCategoria())
                            .build())
                    .toTable(Named.as("AGRUPADOR"), Materialized.as("PRODUCTOS_AGRUPADOS_TABLE"));

            return agrupadorKTable.join(ventaKTable, (agrupadorValue, ventaValue) -> {
                Optional<Producto> matchingProducto = agrupadorValue.getProductos().stream()
                        .filter(p -> p.getProducto().equals(ventaValue.getProducto()))
                        .findFirst();

                        if (matchingProducto.isPresent()) {
                            Producto producto = matchingProducto.get();
                            log.info("Producto encontrado: {}", producto.getProducto());

                            if (producto.getPromocionTimestamp() != null &&
                                    producto.getPromocionTimestamp() > ventaValue.getVentaTimestamp()) {
                                log.info("Promoción es posterior a la venta. No se aplicará.");
                                return VentasProductosMateValue.newBuilder()
                                        .setIdentificador(ventaValue.getIdentificador())
                                        .setProducto(ventaValue.getProducto())
                                        .setCantidad(ventaValue.getCantidad())
                                        .setPrecioConImpuesto(producto.getPrecioConImpuesto())
                                        .setPrecioPromocionado(producto.getPrecioConImpuesto())
                                        .build();
                            } else {
                                return VentasProductosMateValue.newBuilder()
                                        .setIdentificador(producto.getIdentificador())
                                        .setProducto(producto.getProducto())
                                        .setCantidad(ventaValue.getCantidad())
                                        .setPrecioConImpuesto(producto.getPrecioConImpuesto())
                                        .setPrecioPromocionado(producto.getPrecioPromocionado())
                                        .build();
                            }
                        } else {
                            log.warn("No se encontró un producto coincidente para ventaValue: {}", ventaValue);
                            return null;
                        }
                    })
                    .toStream()
                    .peek((k, v) -> {
                        if (v == null) {
                            log.warn("El valor es nulo para la clave: {}", k);
                        } else {
                            log.info("Creadas las promociones -> clave: {} valor: {}", k, v);
                        }
                    });
        };
    }

}
