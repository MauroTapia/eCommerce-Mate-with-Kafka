package com.hiberus.cursos.enviadorproductos.exception;

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


    @Test
    void testHandleDatosProductoInvalidosException() {

        DatosProductoInvalidosException exception = new DatosProductoInvalidosException("El producto no puede ser nulo");

        ResponseEntity<String> response = globalExceptionHandler.handleCantidadInvalida(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El producto no puede ser nulo", response.getBody());
    }

    @Test
    void testHandleDatosIdentificadorInvalidosException() {
        DatosIdentificadorInvalidosException exception = new DatosIdentificadorInvalidosException("El identificador no puede ser nulo");

        ResponseEntity<String> response = globalExceptionHandler.handleDatosVentaInvalidos(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El identificador no puede ser nulo", response.getBody());
    }

    @Test
    void testHandlePrecioInvalidoException() {
        PrecioInvalidoException exception = new PrecioInvalidoException("El precio debe ser mayor a cero");

        ResponseEntity<String> response = globalExceptionHandler.handleCategoriaNoEncontrada(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El precio debe ser mayor a cero", response.getBody());
    }

    @Test
    void testHandleProductoInvalidoException() {
        ProductoInvalidoException exception = new ProductoInvalidoException("El nombre del producto no puede ser nulo o vacío");

        ResponseEntity<String> response = globalExceptionHandler.handleProductoInvalido(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El nombre del producto no puede ser nulo o vacío", response.getBody());
    }

    @Test
    void testHandleCategoriaNoEncontradaException() {
        CategoriaNoEncontradoException exception = new CategoriaNoEncontradoException("La categoría no puede ser nula o vacía");

        ResponseEntity<String> response = globalExceptionHandler.handleCategoriaNoEncontrada(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("La categoría no puede ser nula o vacía", response.getBody());
    }

    @Test
    void testHandleGeneralException() {
        Exception exception = new Exception("Error inesperado");

        ResponseEntity<String> response = globalExceptionHandler.handleGeneralException(exception);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error interno del servidor: Error inesperado", response.getBody());
    }
}
