package com.hiberus.cursos.consultadormates.kafka.mapper;


import com.hiberus.cursos.consultadormates.model.Producto;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionado;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionadoKey;
import com.hiberus.cursos.enviadorproductos.avro.ProductoPromocionadoValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductoMapperTest {

    private ProductoMapper productoMapper;

    @BeforeEach
    void setUp() {
        productoMapper = new ProductoMapper();
    }

    @Test
    void testToDTO() {
        ProductoPromocionadoKey categoriaKey = Mockito.mock(ProductoPromocionadoKey.class);
        Mockito.when(categoriaKey.getCategoria()).thenReturn("Electrónica");

        ProductoPromocionado producto1 = Mockito.mock(ProductoPromocionado.class);
        Mockito.when(producto1.getIdentificador()).thenReturn("123");
        Mockito.when(producto1.getProducto()).thenReturn("Producto A");
        Mockito.when(producto1.getPrecioConImpuesto()).thenReturn(120.0);
        Mockito.when(producto1.getPrecioPromocionado()).thenReturn(100.0);
        Mockito.when(producto1.getPromocionTimestamp()).thenReturn(1633036800L);

        ProductoPromocionado producto2 = Mockito.mock(ProductoPromocionado.class);
        Mockito.when(producto2.getIdentificador()).thenReturn("124");
        Mockito.when(producto2.getProducto()).thenReturn("Producto B");
        Mockito.when(producto2.getPrecioConImpuesto()).thenReturn(150.0);
        Mockito.when(producto2.getPrecioPromocionado()).thenReturn(120.0);
        Mockito.when(producto2.getPromocionTimestamp()).thenReturn(1633036900L);

        Map<String, List<ProductoPromocionado>> productosPorCategoria = new HashMap<>();
        productosPorCategoria.put("Electrónica", List.of(producto1, producto2));

        ProductoPromocionadoValue value = Mockito.mock(ProductoPromocionadoValue.class);
        Mockito.when(value.getProductosPorCategoria()).thenReturn(productosPorCategoria);

        List<Producto> productosDTO = productoMapper.toDTO(categoriaKey, value);

        assertNotNull(productosDTO);
        assertEquals(2, productosDTO.size());

        Producto productoMapped1 = productosDTO.get(0);
        assertEquals("123", productoMapped1.getIdentificador());
        assertEquals("Electrónica", productoMapped1.getCategoria());
        assertEquals("Producto A", productoMapped1.getProducto());
        assertEquals(120.0, productoMapped1.getPrecioConImpuesto());
        assertEquals(100.0, productoMapped1.getPrecioPromocionado());
        assertEquals(1633036800L, productoMapped1.getPromocionTimestamp());

        Producto productoMapped2 = productosDTO.get(1);
        assertEquals("124", productoMapped2.getIdentificador());
        assertEquals("Electrónica", productoMapped2.getCategoria());
        assertEquals("Producto B", productoMapped2.getProducto());
        assertEquals(150.0, productoMapped2.getPrecioConImpuesto());
        assertEquals(120.0, productoMapped2.getPrecioPromocionado());
        assertEquals(1633036900L, productoMapped2.getPromocionTimestamp());
    }
}
