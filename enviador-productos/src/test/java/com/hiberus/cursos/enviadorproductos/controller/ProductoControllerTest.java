package com.hiberus.cursos.enviadorproductos.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiberus.cursos.enviadorproductos.dto.ProductoDTO;
import com.hiberus.cursos.enviadorproductos.exception.CategoriaNoEncontradoException;
import com.hiberus.cursos.enviadorproductos.exception.GlobalExceptionHandler;
import com.hiberus.cursos.enviadorproductos.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {

    @InjectMocks
    private ProductoControllerImpl productoController;

    @Mock
    private ProductoService productoService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(productoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testCrearProductoExito() throws Exception {
        ProductoDTO productoDTO = new ProductoDTO(
                "123",
                "Producto 1",
                10.0,
                "Categoria A");

        doNothing().when(productoService).crear(any(ProductoDTO.class));

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isCreated());

        verify(productoService, times(1)).crear(any(ProductoDTO.class));
    }

    @Test
    public void testDatoVacioException() throws Exception {
        ProductoDTO productoDTO = new ProductoDTO(
                "123",
                "Producto 1",
                10.0,
                "");

        doThrow(new CategoriaNoEncontradoException("La categoría no puede ser nula o vacía"))
                .when(productoService).crear(any(ProductoDTO.class));

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La categoría no puede ser nula o vacía"));

        verify(productoService, times(1)).crear(any(ProductoDTO.class));
    }

}