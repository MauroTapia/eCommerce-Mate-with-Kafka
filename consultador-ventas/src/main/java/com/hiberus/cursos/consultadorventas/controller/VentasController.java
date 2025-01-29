package com.hiberus.cursos.consultadorventas.controller;

import com.hiberus.cursos.consultadorventas.model.Venta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VentasController {
    @Operation(summary = "Obtiene las ventas de Mate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Devuelve todas las ventas de mate")
    })
    ResponseEntity<List<Venta>> getVentas();

}
