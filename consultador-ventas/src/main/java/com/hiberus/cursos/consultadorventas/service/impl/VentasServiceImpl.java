package com.hiberus.cursos.consultadorventas.service.impl;


import com.hiberus.cursos.consultadorventas.model.Venta;
import com.hiberus.cursos.consultadorventas.repository.VentaRepository;
import com.hiberus.cursos.consultadorventas.service.VentasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VentasServiceImpl implements VentasService {

    @Autowired
    VentaRepository ventaRepository;

    @Override
    public List<Venta> getVentas() {
        return ventaRepository.findAll();
    }

    public void guardarVenta(Venta venta) {
        ventaRepository.save(venta);
    }
}