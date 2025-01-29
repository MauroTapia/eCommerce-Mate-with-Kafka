package com.hiberus.cursos.enviadorproductos.controller;


import com.hiberus.cursos.enviadorproductos.dto.ProductoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface ProductoController {
    @Operation(summary = "Crea un producto")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Peticion aceptada")
    })
    ResponseEntity<String> crearProducto(ProductoDTO productoDTO);

}
