package com.hiberus.cursos.consultadormates.controller;


import com.hiberus.cursos.consultadormates.model.Producto;
import com.hiberus.cursos.consultadormates.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/productosMate")
public class ProductoControllerImpl implements ProductoController {

    @Autowired
    ProductoService productoService;

    @Override
    @GetMapping("")
    public ResponseEntity<List<Producto>> getProductos() {
        try {
            log.info("Recibiendo solicitud para obtener todos los productos");
            List<Producto> productos = productoService.getProductos();
            return new ResponseEntity<>(productos, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.warn("Error al obtener productos: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error inesperado al obtener productos: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

