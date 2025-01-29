package com.hiberus.cursos.enviadorpromocion.service.mapper;

public interface Mapper<T, D> {

    T dtoToEntity(D dto);

}
