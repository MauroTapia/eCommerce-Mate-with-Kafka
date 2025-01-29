package com.hiberus.cursos.enviadorventas.service;

import com.hiberus.cursos.enviadorventas.avro.VentasKey;
import com.hiberus.cursos.enviadorventas.avro.VentasValue;
import com.hiberus.cursos.enviadorventas.dto.VentasDTO;
import com.hiberus.cursos.enviadorventas.exception.CantidadInvalidaException;
import com.hiberus.cursos.enviadorventas.exception.CategoriaNoEncontradoException;
import com.hiberus.cursos.enviadorventas.exception.DatosVentaInvalidosException;
import com.hiberus.cursos.enviadorventas.exception.ProductoNoEncontradoException;
import com.hiberus.cursos.enviadorventas.service.impl.VentasServiceImpl;
import com.hiberus.cursos.enviadorventas.service.mapper.VentaKeyMapper;
import com.hiberus.cursos.enviadorventas.service.mapper.VentaValueMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentasServiceImplTest {

    @Mock
    private KafkaTemplate<VentasKey, VentasValue> kafkaTemplate;

    @InjectMocks
    private VentasServiceImpl ventasService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(ventasService, "ventasTopic", "test-topic");
    }


    @Test
    void testCrearEnvioExitoso() {
        VentasDTO ventasDTO = new VentasDTO();
        ventasDTO.setCategoria("Electrónica");
        ventasDTO.setIdentificador("12345");
        ventasDTO.setCantidad(1);
        ventasDTO.setProducto("Laptop");

        VentasKey key = new VentaKeyMapper().dtoToEntity(ventasDTO);
        VentasValue value = new VentaValueMapper().dtoToEntity(ventasDTO);

        ventasService.crear(ventasDTO);

        verify(kafkaTemplate, times(1)).send(eq("test-topic"), eq(key), eq(value));
    }

    @Test
    void testCrearVentasDTONulo() {
        DatosVentaInvalidosException exception = assertThrows(DatosVentaInvalidosException.class, () -> {
            ventasService.crear(null);
        });

        assertEquals("La venta no puede ser nula", exception.getMessage());
    }

    @Test
    void testCrearCategoriaNula() {
        VentasDTO ventasDTO = new VentasDTO();
        ventasDTO.setIdentificador("12345");
        ventasDTO.setCantidad(1);
        ventasDTO.setProducto("Laptop");

        CategoriaNoEncontradoException exception = assertThrows(CategoriaNoEncontradoException.class, () -> {
            ventasService.crear(ventasDTO);
        });

        assertEquals("La categoría no puede ser nula o vacía", exception.getMessage());
    }

    @Test
    void testCrearProductoNulo() {
        VentasDTO ventasDTO = new VentasDTO();
        ventasDTO.setCategoria("Electrónica");
        ventasDTO.setIdentificador("12345");
        ventasDTO.setCantidad(1);

        ProductoNoEncontradoException exception = assertThrows(ProductoNoEncontradoException.class, () -> {
            ventasService.crear(ventasDTO);
        });

        assertEquals("El producto no puede ser nulo o vacío", exception.getMessage());
    }

    @Test
    void testCrearCantidadInvalida() {
        VentasDTO ventasDTO = new VentasDTO();
        ventasDTO.setCategoria("Electrónica");
        ventasDTO.setIdentificador("12345");
        ventasDTO.setProducto("Laptop");
        ventasDTO.setCantidad(0);

        CantidadInvalidaException exception = assertThrows(CantidadInvalidaException.class, () -> {
            ventasService.crear(ventasDTO);
        });

        assertEquals("La cantidad debe ser mayor a cero", exception.getMessage());
    }
}
