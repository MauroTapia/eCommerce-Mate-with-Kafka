package com.hiberus.cursos.consultadormates.kafka.mapper;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ProductoMapperTest {

    private final ProductoMapper productoMapper = new ProductoMapper();
/*
    @Test
    public void testToDTO() {
        AgrupadorCategoriaKey categoriaKey = new AgrupadorCategoriaKey();
        categoriaKey.setCategoria("Categoria1");

        Producto productoAvro = new Producto();
        productoAvro.setProducto("Producto1");
        productoAvro.setIdentificador("123");
        productoAvro.setPrecioConImpuesto(100.0);
        productoAvro.setPrecioPromocionado(90.0);

        AgrupadorCategoriaValue categoriaValue = new AgrupadorCategoriaValue();
        categoriaValue.setProductos(List.of(productoAvro));

        List<Producto> productosDTO = productoMapper.toDTO(categoriaKey, categoriaValue);

        assertNotNull(productosDTO);
        assertEquals(1, productosDTO.size());
        Producto productoDTO = productosDTO.get(0);

        assertEquals("Categoria1", productoDTO.getCategoria());
        assertEquals("Producto1", productoDTO.getProducto());
        assertEquals("123", productoDTO.getIdentificador());
        assertEquals(100.0, productoDTO.getPrecioConImpuesto());
        assertEquals(90.0, productoDTO.getPrecioPromocionado());
    }*/
}
