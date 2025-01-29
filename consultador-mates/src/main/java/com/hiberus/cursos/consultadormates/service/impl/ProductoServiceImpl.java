package com.hiberus.cursos.consultadormates.service.impl;

import com.hiberus.cursos.consultadormates.model.Producto;
import com.hiberus.cursos.consultadormates.repository.ProductoRepository;
import com.hiberus.cursos.consultadormates.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> getProductos() {
        log.info("Obteniendo todos los productos");

        List<Producto> productos = productoRepository.findAll();

        if (productos.isEmpty()) {
            log.warn("No se encontraron productos en la base de datos");
            throw new RuntimeException("No se encontraron productos");
        }

        log.info("Se encontraron {} productos", productos.size());
        return productos;
    }

    public void guardarProductoMates(Producto producto) {
        Optional<Producto> existente = productoRepository.findByCategoria(producto.getIdentificador());
        if (existente.isPresent()) {
            Producto actual = existente.get();
            actual.setProducto(producto.getProducto());
            actual.setPrecioConImpuesto(producto.getPrecioConImpuesto());
            actual.setPrecioPromocionado(producto.getPrecioPromocionado());
            productoRepository.save(actual);
        } else {
            productoRepository.save(producto);
        }
    }
}