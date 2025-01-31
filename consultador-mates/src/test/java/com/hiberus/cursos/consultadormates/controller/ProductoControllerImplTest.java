package com.hiberus.cursos.consultadormates.controller;

import com.hiberus.cursos.consultadormates.model.Producto;
import com.hiberus.cursos.consultadormates.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductoControllerImpl.class)
class ProductoControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoServiceImpl productoService;

    @Test
    void testGetProductosExitoso() throws Exception {
        List<Producto> productos = Arrays.asList(new Producto("01", "mate", "mate imperial", 10.0, 10.0, 10L),
                new Producto("02", "mate 02", "mate imperial", 11.0, 11.0, 10L));

        Mockito.when(productoService.getProductos()).thenReturn(productos);

        mockMvc.perform(get("/productosMate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(productos.size()))
                .andExpect(jsonPath("$[0].identificador").value("01"))
                .andExpect(jsonPath("$[0].producto").value("mate imperial"))
                .andExpect(jsonPath("$[1].identificador").value("02"))
                .andExpect(jsonPath("$[1].producto").value("mate imperial"));
    }

    @Test
    void testGetProductosVacios() throws Exception {
        Mockito.when(productoService.getProductos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/productosMate"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }


    @Test
    void testGetProductosErrorGeneral() throws Exception {
        Mockito.when(productoService.getProductos()).thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(get("/productosMate"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(""));
    }

}
