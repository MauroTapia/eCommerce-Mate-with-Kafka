package com.hiberus.cursos.consultadorventas.controller;


import com.hiberus.cursos.consultadorventas.model.Venta;
import com.hiberus.cursos.consultadorventas.service.VentasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/ventasMate")
public class VentasControllerImpl implements VentasController {

    @Autowired
    private VentasService ventasService;

    @Override
    @GetMapping("")
    public ResponseEntity<List<Venta>> getVentas() {
        try {
            List<Venta> ventas = ventasService.getVentas();

            if (ventas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            log.error("Error al obtener las ventas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
