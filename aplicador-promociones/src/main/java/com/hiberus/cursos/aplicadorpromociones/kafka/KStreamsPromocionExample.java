package com.hiberus.cursos.aplicadorpromociones.kafka;

import com.hiberus.cursos.enviadorproductos.avro.ProductoConImpuestoValue;
import com.hiberus.cursos.enviadorproductos.avro.ProductoKey;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionadoKey;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionadoValue;
import com.hiberus.cursos.enviadorpromocion.avro.PromocionKey;
import com.hiberus.cursos.enviadorpromocion.avro.PromocionValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BiFunction;

@SpringBootApplication
@Slf4j
public class KStreamsPromocionExample {

    public static void main(final String[] args) {
        SpringApplication.run(KStreamsPromocionExample.class, args);
    }

    @Bean
    public BiFunction<KStream<ProductoKey, ProductoConImpuestoValue>,
            KStream<PromocionKey, PromocionValue>,
            KStream<ProductoPromocionadoKey, ProductoPromocionadoValue>> joiner() {
        return (productosStream, promocionesStream) -> {
            KTable<ProductoPromocionadoKey, PromocionValue> promocionKTable = promocionesStream
                    .selectKey((k, v) -> ProductoPromocionadoKey.newBuilder()
                            .setCategoria(v.getCategoria())
                            .build())
                    .toTable(Named.as("PROMOCIONES"), Materialized.as("PROMOCIONES_TABLE"));

            KTable<ProductoPromocionadoKey, ProductoConImpuestoValue> productoKTable = productosStream
                    .selectKey((k, v) -> ProductoPromocionadoKey.newBuilder()
                            .setCategoria(v.getCategoria())
                            .build())
                    .toTable(Named.as("PRODUCTOS-TRANSFORMADOS"), Materialized.as("PRODUCTOS_TRANSFORMADOS_TABLE"));

            return productoKTable.outerJoin(promocionKTable,
                            (productoValue, promocionValue) -> {
                                if (productoValue == null) {
                                    return null;
                                }
                                long promocionTimestamp = System.currentTimeMillis();

                                double precioPromocionado = (promocionValue != null)
                                        ? calcularPrecioPromocionado(productoValue, promocionValue)
                                        : productoValue.getPrecioConImpuesto();

                                return ProductoPromocionadoValue.newBuilder()
                                        .setIdentificador(productoValue.getIdentificador())
                                        .setCategoria(productoValue.getCategoria())
                                        .setProducto(productoValue.getProducto())
                                        .setPrecioConImpuesto(productoValue.getPrecioConImpuesto())
                                        .setPrecioPromocionado(precioPromocionado)
                                        .setPromocionTimestamp(promocionTimestamp)
                                        .build();
                            })
                    .toStream()
                    .peek((k, v) -> log.info("Creadas las promociones -> clave: {} valor: {}", k, v));
        };
    }

    private double calcularPrecioPromocionado(ProductoConImpuestoValue productoValue, PromocionValue promocionValue) {
        if (promocionValue != null && promocionValue.getDescuento() > 0) {
            double descuento = promocionValue.getDescuento();
            return productoValue.getPrecioConImpuesto() * (1 - descuento / 100);
        }
        return redondear(productoValue.getPrecioConImpuesto());
    }

    private double redondear(double valor) {
        BigDecimal bd = new BigDecimal(valor).setScale(0, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}