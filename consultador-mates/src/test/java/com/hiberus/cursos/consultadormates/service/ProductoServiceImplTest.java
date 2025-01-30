package com.hiberus.cursos.consultadormates.service;

import com.hiberus.cursos.consultadormates.model.Producto;
import com.hiberus.cursos.consultadormates.repository.ProductoRepository;
import com.hiberus.cursos.consultadormates.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;
    /*
    @Test
    void testGetProductosExitoso() {
        Producto producto1 = new Producto("123", "01", "mate 1", 12.0, 12.0);
        Producto producto2 = new Producto("124", "02", "mate 2", 10.0, 10.0);

        Mockito.when(productoRepository.findAll()).thenReturn(Arrays.asList(producto1, producto2));

        List<Producto> productos = productoService.getProductos();

        assertNotNull(productos);
        assertEquals(2, productos.size());
        assertEquals("mate 1", productos.get(0).getProducto());
        assertEquals("mate 2", productos.get(1).getProducto());
    }

    @Test
    void testGetProductosSinProductos() {
        Mockito.when(productoRepository.findAll()).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productoService.getProductos());
        assertEquals("No se encontraron productos", exception.getMessage());
    }

    @Test
    void testGuardarProductoMatesProductoExistente() {
        Producto productoDTO = new Producto("123", "01", "mate 1", 15.0, 15.0);

        Producto productoExistente = new Producto("123", "01", "mate 1", 12.0, 12.0);
        Mockito.when(productoRepository.findByCategoria(productoDTO.getCategoria())).thenReturn(Optional.of(productoExistente));

        productoService.guardarProductoMates(productoDTO);

        Mockito.verify(productoRepository, Mockito.times(1)).save(productoExistente);
        assertEquals(15.0, productoExistente.getPrecioConImpuesto());
        assertEquals(15.0, productoExistente.getPrecioPromocionado());
    }

    @Test
    void testGuardarProductoMatesProductoNuevo() {
        Producto productoDTO = new Producto("125", "03", "mate 3", 20.0, 20.0);

        Mockito.when(productoRepository.findByCategoria(productoDTO.getCategoria())).thenReturn(Optional.empty());

        productoService.guardarProductoMates(productoDTO);

        Mockito.verify(productoRepository, Mockito.times(1)).save(productoDTO);
    }

     */
}
