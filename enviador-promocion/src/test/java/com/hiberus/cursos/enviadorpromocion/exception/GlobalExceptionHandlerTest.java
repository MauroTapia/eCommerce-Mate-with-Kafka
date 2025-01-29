package com.hiberus.cursos.enviadorpromocion.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testHandleDatosPromocionInvalidos() {
        DatosPromocionInvalidosException exception = new DatosPromocionInvalidosException("La promoción no puede ser nula");
        ResponseEntity<String> response = globalExceptionHandler.handleDatosPromocionInvalidos(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("La promoción no puede ser nula", response.getBody());
    }

    @Test
    void testHandleCategoriaNoEncontrada() {
        CategoriaNoEncontradoException exception = new CategoriaNoEncontradoException("La categoría no puede ser nula o vacía");
        ResponseEntity<String> response = globalExceptionHandler.handleCategoriaNoEncontrada(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("La categoría no puede ser nula o vacía", response.getBody());
    }

    @Test
    void testHandlePromocionNoEncontrada() {
        PromocionNoEncontradoException exception = new PromocionNoEncontradoException("El nombre de la promoción no puede ser nulo o vacío");
        ResponseEntity<String> response = globalExceptionHandler.handleProductoNoEncontrado(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El nombre de la promoción no puede ser nulo o vacío", response.getBody());
    }

    @Test
    void testHandleDescuentoInvalido() {
        DescuentoInvalidaException exception = new DescuentoInvalidaException("El descuento debe ser igual o mayor a cero");
        ResponseEntity<String> response = globalExceptionHandler.handleDatosVentaInvalidos(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El descuento debe ser igual o mayor a cero", response.getBody());
    }

    @Test
    void testHandleGeneralException() {
        Exception exception = new Exception("Error inesperado");
        ResponseEntity<String> response = globalExceptionHandler.handleGeneralException(exception);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error interno del servidor: Error inesperado", response.getBody());
    }
}


