package com.hiberus.cursos.transformadorProductos.kafka;

import com.hiberus.cursos.enviadorproductos.avro.ProductoConImpuestoValue;
import com.hiberus.cursos.enviadorproductos.avro.ProductoKey;
import com.hiberus.cursos.enviadorproductos.avro.ProductoValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class TransformProductIVAService {

    @Value("${environment.productos-impuesto-topic}")
    private String productosImpuestoTopic;
    @Autowired
    private KafkaTemplate<ProductoKey, ProductoConImpuestoValue> kafkaTemplate;

    private static final double IVA_RATE = 0.21;
    public double redondear(double valor) {
        BigDecimal bd = new BigDecimal(valor).setScale(0, RoundingMode.HALF_UP);
        log.info("Valor original: {}, Valor redondeado: {}", valor, bd.doubleValue());
        return bd.doubleValue();
    }

    public void transformar(ProductoKey key, ProductoValue value) {
        double precioBase = value.getPrecio();
        double precioConIVA = redondear(precioBase * (1 + IVA_RATE));

        ProductoConImpuestoValue productoConImpuesto = ProductoConImpuestoValue.newBuilder()
                .setIdentificador(value.getIdentificador())
                .setProducto(value.getProducto())
                .setPrecioConImpuesto(precioConIVA)
                .setCategoria(value.getCategoria())
                .build();

        log.info("Enviando producto con impuesto al topic '{}': Key -> {}, Producto -> {}, Precio con IVA -> {}",
                productosImpuestoTopic, key, productoConImpuesto.getProducto(), productoConImpuesto.getPrecioConImpuesto());

        kafkaTemplate.send(productosImpuestoTopic, key, productoConImpuesto);
    }
}