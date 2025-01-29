package com.hiberus.cursos.consultadorventas.repository;

import com.hiberus.cursos.consultadorventas.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, String> {

}
