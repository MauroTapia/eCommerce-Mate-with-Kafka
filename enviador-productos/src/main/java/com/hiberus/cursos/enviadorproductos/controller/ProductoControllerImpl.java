package com.hiberus.cursos.enviadorproductos.controller;


import com.hiberus.cursos.enviadorproductos.dto.ProductoDTO;
import com.hiberus.cursos.enviadorproductos.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(value = "/productos")
public class ProductoControllerImpl implements ProductoController {

    @Autowired
    ProductoService productoService;

    @Override
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public ResponseEntity<String> crearProducto(@RequestBody ProductoDTO productoDTO) {
        log.debug("Recibida peticion de crear un producto");
        productoService.crear(productoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto creado correctamente");
    }
}
