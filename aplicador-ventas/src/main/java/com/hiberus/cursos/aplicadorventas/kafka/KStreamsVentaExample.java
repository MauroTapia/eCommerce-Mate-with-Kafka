package com.hiberus.cursos.aplicadorventas.kafka;

import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionado;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionadoKey;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionadoValue;
import com.hiberus.cursos.enviadorventas.avro.VentasKey;
import com.hiberus.cursos.enviadorventas.avro.VentasValue;
import com.hiberus.cursos.mix.avro.VentasProductosMateKey;
import com.hiberus.cursos.mix.avro.VentasProductosMateValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

        return (productosStream, ventasStream) -> {

            KTable<VentasProductosMateKey, ProductoPromocionadoValue> productosKTable = productosStream
                    .selectKey((k, v) -> VentasProductosMateKey.newBuilder()
                            .setCategoria(k.getCategoria())
                            .build())
                    .toTable(Named.as("PRODUCTOS_PROMOCIONADOS"), Materialized.as("PRODUCTOS_PROMOCIONADOS_TABLE"));

            KTable<VentasProductosMateKey, VentasValue> ventasKTable = ventasStream
                    .selectKey((k, v) -> VentasProductosMateKey.newBuilder()
                            .setCategoria(k.getCategoria())  // Usamos categoria del VentasKey
                            .build())
                    .toTable(Named.as("VENTAS"), Materialized.as("VENTAS_TABLE"));

            return productosKTable.join(ventasKTable, (productoPromocionadoValue, ventaValue) -> {

                        ProductoPromocionado matchingProducto = null;
                        for (Map.Entry<String, List<ProductoPromocionado>> entry : productoPromocionadoValue.getProductosPorCategoria().entrySet()) {
                            for (ProductoPromocionado producto : entry.getValue()) {
                                if (producto.getProducto().equals(ventaValue.getProducto())) {
                                    matchingProducto = producto;
                                    break;
                                }
                            }
                            if (matchingProducto != null) {
                                break;
                            }
                        }

                        if (matchingProducto != null) {
                            log.info("Producto encontrado: {}", matchingProducto.getProducto());

                            Optional<Long> promocionTimestamp = Optional.ofNullable(matchingProducto.getPromocionTimestamp());
                            Optional<Long> ventaTimestamp = Optional.ofNullable(ventaValue.getVentaTimestamp());

                            if (promocionTimestamp.isPresent() && ventaTimestamp.isPresent() && promocionTimestamp.get() > ventaTimestamp.get()) {
                                log.info("Promoción es posterior a la venta. No se aplicará.");

                                return VentasProductosMateValue.newBuilder()
                                        .setIdentificador(ventaValue.getIdentificador())
                                        .setProducto(ventaValue.getProducto())
                                        .setCantidad(ventaValue.getCantidad())
                                        .setPrecioConImpuesto(matchingProducto.getPrecioConImpuesto())
                                        .setPrecioPromocionado(matchingProducto.getPrecioConImpuesto())
                                        .build();
                            } else {
                                return VentasProductosMateValue.newBuilder()
                                        .setIdentificador(matchingProducto.getIdentificador())
                                        .setProducto(matchingProducto.getProducto())
                                        .setCantidad(ventaValue.getCantidad())
                                        .setPrecioConImpuesto(matchingProducto.getPrecioConImpuesto())
                                        .setPrecioPromocionado(matchingProducto.getPrecioPromocionado())
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
                            log.info("Producto con promoción creada -> clave: {} valor: {}", k, v);
                        }
                    });
        };
    }
}