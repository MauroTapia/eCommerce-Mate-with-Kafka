package com.hiberus.cursos.enviadorpromocion.controller;


import com.hiberus.cursos.enviadorpromocion.dto.PromocionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface PromocionController {
    @Operation(summary = "Crea una promocion")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Peticion aceptada")
    })
    ResponseEntity<String> crearPromocion(PromocionDTO promocionDTO);

}
