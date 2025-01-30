package com.hiberus.cursos.agrupadorProductoMates.kafka;

import com.hiberus.cursos.enviadorproductos.avro.AgrupadorCategoriaValue;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Initializer implements org.apache.kafka.streams.kstream.Initializer<AgrupadorCategoriaValue> {
    @Override
    public AgrupadorCategoriaValue apply() {
        return AgrupadorCategoriaValue.newBuilder()
                .setProductosPorCategoria(new HashMap<>())
                .build();
    }
}
