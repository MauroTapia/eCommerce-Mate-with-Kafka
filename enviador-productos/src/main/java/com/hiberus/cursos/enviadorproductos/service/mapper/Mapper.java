package com.hiberus.cursos.enviadorproductos.service.mapper;

public interface Mapper<T, D> {

    T dtoToEntity(D dto);

}
