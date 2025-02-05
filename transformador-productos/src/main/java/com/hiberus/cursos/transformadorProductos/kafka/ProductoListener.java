package com.hiberus.cursos.transformadorProductos.kafka;


import com.hiberus.cursos.enviadorproductos.avro.ProductoKey;
import com.hiberus.cursos.enviadorproductos.avro.ProductoValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@Slf4j
public class ProductoListener  {

    @Autowired
    private TransformProductIVAService transformProductIVAService;

    @KafkaListener(topics = "productos")
    public void process(ConsumerRecord<ProductoKey, ProductoValue> producto) {
        log.info("Nuevo producto recibido. Datos internos: topic -> {}, partition -> {}, offset -> {}, key: {}",
                producto.topic(), producto.partition(), producto.offset(), producto.key());

        transformProductIVAService.transformar(producto.key(), producto.value());
    }
}
