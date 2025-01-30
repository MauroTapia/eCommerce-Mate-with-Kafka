package com.hiberus.cursos.enviadorproductos.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.hiberus.cursos.enviadorproductos.avro.ProductoKey;
import com.hiberus.cursos.enviadorproductos.avro.ProductoValue;
import com.hiberus.cursos.enviadorproductos.dto.ProductoDTO;
import org.junit.jupiter.api.Test;

public class ProductoMapperTest {


    @Test
    public void testProductoKeyMapper() {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setIdentificador("12345");

        ProductoKey productoKey = new ProductoKeyMapper().dtoToEntity(productoDTO);

        assertNotNull(productoKey);
        assertEquals("12345", productoKey.getCategoria());
    }

    @Test
    public void testProductoValueMapper() {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setIdentificador("12345");
        productoDTO.setProducto("Smartphone");
        productoDTO.setPrecio(500.0);
        productoDTO.setCategoria("Electrónica");

        ProductoValue productoValue = new ProductoValueMapper().dtoToEntity(productoDTO);

        assertNotNull(productoValue);
        assertEquals("12345", productoValue.getIdentificador());
        assertEquals("Smartphone", productoValue.getProducto());
        assertEquals(500.0, productoValue.getPrecio());
        assertEquals("Electrónica", productoValue.getCategoria());
    }

    @Test
    public void testProductoValueMapperConValoresNulos() {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setIdentificador("");
        productoDTO.setProducto("");
        productoDTO.setPrecio(0.0);
        productoDTO.setCategoria("");

        ProductoValue productoValue = new ProductoValueMapper().dtoToEntity(productoDTO);

        assertNotNull(productoValue);
        assertEquals("", productoValue.getIdentificador());
        assertEquals("", productoValue.getProducto());
        assertEquals(0.0, productoValue.getPrecio());
        assertEquals("", productoValue.getCategoria());
    }


}