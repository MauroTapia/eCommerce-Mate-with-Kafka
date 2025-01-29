package com.hiberus.cursos.enviadorproductos.service.impl;


import com.hiberus.cursos.enviadorproductos.avro.ProductoKey;

import com.hiberus.cursos.enviadorproductos.avro.ProductoValue;
import com.hiberus.cursos.enviadorproductos.dto.ProductoDTO;
import com.hiberus.cursos.enviadorproductos.exception.*;
import com.hiberus.cursos.enviadorproductos.service.ProductoService;
import com.hiberus.cursos.enviadorproductos.service.mapper.ProductoKeyMapper;
import com.hiberus.cursos.enviadorproductos.service.mapper.ProductoValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    @Value("${environment.producto-topic}")
    private String productoTopic;

    public void setProductoTopic(String productoTopic) {
        this.productoTopic = productoTopic;
    }
    @Autowired
    private KafkaTemplate<ProductoKey, ProductoValue> kafkaTemplate;

    @Override
    public void crear(ProductoDTO productoDTO) {
        try {
            validarProducto(productoDTO);

            ProductoKey key = new ProductoKeyMapper().dtoToEntity(productoDTO);
            ProductoValue value = new ProductoValueMapper().dtoToEntity(productoDTO);

            log.debug("Enviando el producto al topic de kafka");
            kafkaTemplate.send(productoTopic, key, value);
        } catch (RuntimeException e) {
            log.error("Error al procesar la venta: {}", e.getMessage(), e);
            throw e;
        }
    }


    public void validarProducto(ProductoDTO productoDTO) {
        if (productoDTO == null) {
            throw new DatosProductoInvalidosException("El producto no puede ser nulo");
        }
        if (productoDTO.getIdentificador() == null) {
            throw new DatosIdentificadorInvalidosException("El identificador no puede ser nulo");
        }
        if (productoDTO.getProducto() == null || productoDTO.getProducto().isBlank()) {
            throw new ProductoInvalidoException("El nombre del producto no puede ser nulo o vacío");
        }
        if (productoDTO.getPrecio() == null || productoDTO.getPrecio() <= 0) {
            throw new PrecioInvalidoException("El precio debe ser mayor a cero");
        }

        if (productoDTO.getCategoria() == null || productoDTO.getCategoria().isBlank()) {
            log.error("La categoría es nula o vacía");
            throw new CategoriaNoEncontradoException("La categoría no puede ser nula o vacía");
        }
    }

}
