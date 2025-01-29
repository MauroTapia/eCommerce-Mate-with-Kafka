package com.hiberus.cursos.enviadorproductos.service;


import com.hiberus.cursos.enviadorproductos.avro.ProductoKey;
import com.hiberus.cursos.enviadorproductos.avro.ProductoValue;
import com.hiberus.cursos.enviadorproductos.dto.ProductoDTO;
import com.hiberus.cursos.enviadorproductos.exception.*;
import com.hiberus.cursos.enviadorproductos.service.impl.ProductoServiceImpl;
import com.hiberus.cursos.enviadorproductos.service.mapper.ProductoKeyMapper;
import com.hiberus.cursos.enviadorproductos.service.mapper.ProductoValueMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private KafkaTemplate<ProductoKey, ProductoValue> kafkaTemplate;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(productoService, "productoTopic", "test-topic");
    }


    @Test
    public void testCrearProductoExitoso() {
        ProductoDTO productoDTO = new ProductoDTO("123", "Producto Test", 100.0, "Categoria Test");

        ProductoKey key = new ProductoKeyMapper().dtoToEntity(productoDTO);
        ProductoValue value = new ProductoValueMapper().dtoToEntity(productoDTO);
        productoService.crear(productoDTO);

        verify(kafkaTemplate, times(1)).send(eq("test-topic"), eq(key), eq(value));
    }

    @Test
    public void testCrearProductoNulo() {
        // Cuando el productoDTO es nulo
        ProductoDTO productoDTO = null;

        assertThrows(DatosProductoInvalidosException.class, () -> {
            productoService.crear(productoDTO);
        });
    }

    @Test
    public void testCrearProductoSinIdentificador() {
        ProductoDTO productoDTO = new ProductoDTO(null, "Producto Test", 100.0, "Categoria Test");

        assertThrows(DatosIdentificadorInvalidosException.class, () -> {
            productoService.crear(productoDTO);
        });
    }

    @Test
    public void testCrearProductoSinNombre() {
        ProductoDTO productoDTO = new ProductoDTO("123", "", 100.0, "Categoria Test");

        assertThrows(ProductoInvalidoException.class, () -> {
            productoService.crear(productoDTO);
        });
    }

    @Test
    public void testCrearProductoConPrecioInvalido() {
        ProductoDTO productoDTO = new ProductoDTO("123", "Producto Test", -1.0, "Categoria Test");

        assertThrows(PrecioInvalidoException.class, () -> {
            productoService.crear(productoDTO);
        });
    }

    @Test
    public void testCrearProductoSinCategoria() {
        ProductoDTO productoDTO = new ProductoDTO("123", "Producto Test", 100.0, "");

        assertThrows(CategoriaNoEncontradoException.class, () -> {
            productoService.crear(productoDTO);
        });
    }

}