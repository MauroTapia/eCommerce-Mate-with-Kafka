package com.hiberus.cursos.consultadorventas.kafka.mapper;

import java.util.List;

public interface Mapper<K, V , D> {

    List<D> toDTO(K key, V value);

}
