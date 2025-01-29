package com.hiberus.cursos.consultadormates.kafka.mapper;

public interface Mapper<K, V , D> {

    D toDTO(K key, V value);

}
