package com.hiberus.cursos.consultadormates.repository;

import com.hiberus.cursos.consultadormates.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {

    Optional<Producto> findByCategoria(String categoria);
}
