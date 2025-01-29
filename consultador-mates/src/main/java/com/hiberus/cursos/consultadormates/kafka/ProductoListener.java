package com.hiberus.cursos.consultadormates.kafka;

import com.hiberus.cursos.consultadormates.model.Producto;
import com.hiberus.cursos.consultadormates.kafka.mapper.ProductoMapper;
import com.hiberus.cursos.consultadormates.service.ProductoService;
import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaKey;
import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;

@Configuration
@Slf4j
public class ProductoListener {

    @Autowired
    private ProductoService productoService;

    private final ProductoMapper productoMapper = new ProductoMapper();

    @Bean
    public Consumer<KStream<AgrupadorCategoriaKey, AgrupadorCategoriaValue>> process() {
        return productoStream -> productoStream
                .peek((k, v) -> log.info("Recibida categoría con clave: {} y valor: {}", k, v))
                .peek((categoria, value) -> {
                    List<Producto> productos = productoMapper.toDTO(categoria, value);
                    productos.forEach(producto -> {
                        log.info("Guardando producto: {}", producto);
                        productoService.guardarProductoMates(producto);
                    });
                });
    }
}