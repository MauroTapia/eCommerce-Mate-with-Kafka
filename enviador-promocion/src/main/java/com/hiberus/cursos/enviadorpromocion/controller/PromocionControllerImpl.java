package com.hiberus.cursos.enviadorpromocion.controller;


import com.hiberus.cursos.enviadorpromocion.dto.PromocionDTO;
import com.hiberus.cursos.enviadorpromocion.service.PromocionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(value = "/promocion")
public class PromocionControllerImpl implements PromocionController {

    @Autowired
    PromocionService promocionService;

    @Override
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public ResponseEntity<String> crearPromocion(@RequestBody PromocionDTO promocionDTO) {
        log.debug("Recibida peticion de crear una promocion");

        promocionService.crear(promocionDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("Promocion creada correctamente");
    }

}
