package com.hiberus.cursos.consultadorventas.kafka;

import com.hiberus.cursos.consultadorventas.kafka.mapper.VentaMapper;
import com.hiberus.cursos.consultadorventas.model.Venta;
import com.hiberus.cursos.consultadorventas.service.VentasService;
import com.hiberus.cursos.mix.avro.VentasProductosMateKey;
import com.hiberus.cursos.mix.avro.VentasProductosMateValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;

@Configuration
@Slf4j
public class VentaListener {

    @Autowired
    private VentasService ventasService;

    private final VentaMapper ventaMapper = new VentaMapper();

    @Bean
    public Consumer<KStream<VentasProductosMateKey, VentasProductosMateValue>> process() {
        return ventaStream -> ventaStream
                .peek((k, v) -> log.info("Recibida categoría con clave: {}", k.getIdentificadorVenta()))
                .peek((categoriaKey, value) -> {
                    List<Venta> ventas = ventaMapper.toDTO(categoriaKey, value);
                    ventas.forEach(ventasService::guardarVenta);
                });
    }
}
