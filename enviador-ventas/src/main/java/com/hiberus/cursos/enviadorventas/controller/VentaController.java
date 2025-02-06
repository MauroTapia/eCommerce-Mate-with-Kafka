package com.hiberus.cursos.enviadorventas.controller;


import com.hiberus.cursos.enviadorventas.model.VentasRequest;
import com.hiberus.cursos.enviadorventas.model.dto.VentasDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface VentaController {
    @Operation(summary = "Crea una venta")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Peticion aceptada")
    })
    ResponseEntity<String> crearVenta(VentasRequest ventasRequest);

}
