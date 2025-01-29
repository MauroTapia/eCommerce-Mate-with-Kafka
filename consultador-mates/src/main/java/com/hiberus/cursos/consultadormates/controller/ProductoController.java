package com.hiberus.cursos.consultadormates.controller;

import com.hiberus.cursos.consultadormates.model.Producto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductoController {
    @Operation(summary = "Obtiene los productos de Mate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Devuelve todos los productos de mate")
    })
    ResponseEntity<List<Producto>> getProductos();

}
