package com.hiberus.cursos.enviadorventas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiberus.cursos.enviadorventas.controller.VentaControllerImpl;
import com.hiberus.cursos.enviadorventas.dto.VentasDTO;
import com.hiberus.cursos.enviadorventas.exception.CategoriaNoEncontradoException;
import com.hiberus.cursos.enviadorventas.exception.GlobalExceptionHandler;
import com.hiberus.cursos.enviadorventas.service.VentasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class VentaControllerImplTest {

    @InjectMocks
    private VentaControllerImpl ventaController;

    @Mock
    private VentasService ventasService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(ventaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private ObjectMapper objectMapper = new ObjectMapper();



    @Test
    public void testCrearVentaExito() throws Exception {
        VentasDTO ventasDTO = new VentasDTO();
        ventasDTO.setCategoria("Electrónica");
        ventasDTO.setIdentificador("12345");
        ventasDTO.setCantidad(1);
        ventasDTO.setProducto("Smartphone");

        doNothing().when(ventasService).crear(any(VentasDTO.class));

        mockMvc.perform(post("/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ventasDTO)))
                .andExpect(status().isCreated());

        verify(ventasService, times(1)).crear(any(VentasDTO.class));
    }

    @Test
    public void testDatoVacioException() throws Exception {
        VentasDTO ventasDTO = new VentasDTO();
        ventasDTO.setCategoria("");
        ventasDTO.setProducto("Smartphone");
        ventasDTO.setCantidad(1);

        doThrow(new CategoriaNoEncontradoException("La categoría no puede ser nula o vacía"))
                .when(ventasService).crear(any(VentasDTO.class));

        mockMvc.perform(post("/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ventasDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La categoría no puede ser nula o vacía"));

        verify(ventasService, times(1)).crear(any(VentasDTO.class));
    }
}
