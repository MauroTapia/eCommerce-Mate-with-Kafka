package com.hiberus.cursos.enviadorventas.controller;


import com.hiberus.cursos.enviadorventas.model.VentasRequest;
import com.hiberus.cursos.enviadorventas.model.dto.VentasDTO;
import com.hiberus.cursos.enviadorventas.service.VentasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(value = "/ventas")
public class VentaControllerImpl implements VentaController {

    @Autowired
    VentasService ventasService;

    @Override
    @PostMapping
    public ResponseEntity<String> crearVenta(@RequestBody VentasRequest ventasRequest) {
        log.debug("Recibida petición de crear una venta");

        ventasService.crear(ventasRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("Venta creada correctamente");
    }
}
