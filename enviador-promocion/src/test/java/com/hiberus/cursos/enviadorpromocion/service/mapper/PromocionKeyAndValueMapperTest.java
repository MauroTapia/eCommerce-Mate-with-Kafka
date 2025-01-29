package com.hiberus.cursos.enviadorpromocion.service.mapper;
import static org.junit.jupiter.api.Assertions.*;

import com.hiberus.cursos.enviadorpromocion.avro.PromocionKey;
import com.hiberus.cursos.enviadorpromocion.avro.PromocionValue;
import com.hiberus.cursos.enviadorpromocion.dto.PromocionDTO;
import org.junit.jupiter.api.Test;

public class PromocionKeyAndValueMapperTest {

    private final PromocionKeyMapper promocionKeyMapper = new PromocionKeyMapper();

    private final PromocionValueMapper promocionValueMapper = new PromocionValueMapper();

    @Test
    public void testPromocionKeyMapper() {
        PromocionDTO promocionDTO = new PromocionDTO();
        promocionDTO.setNombre("Promocion 1");
        promocionDTO.setDescuento(10.0);
        promocionDTO.setCategoria("Categoria Test");

        PromocionKey promocionKey = promocionKeyMapper.dtoToEntity(promocionDTO);

        assertNotNull(promocionKey, "La PromocionKey no debería ser nula.");
        assertEquals("Promocion 1", promocionKey.getNombre(), "El nombre de la PromocionKey debería ser el mismo que en el DTO.");
    }

    @Test
    public void testPromocionValueMapper() {
        PromocionDTO promocionDTO = new PromocionDTO();
        promocionDTO.setNombre("Promocion 1");
        promocionDTO.setDescuento(10.0);
        promocionDTO.setCategoria("Categoria Test");

        PromocionValue promocionValue = promocionValueMapper.dtoToEntity(promocionDTO);

        assertNotNull(promocionValue, "La PromocionValue no debería ser nula.");
        assertEquals("Promocion 1", promocionValue.getNombre(), "El nombre de la PromocionValue debería ser el mismo que en el DTO.");
        assertEquals(10.0, promocionValue.getDescuento(), "El descuento de la PromocionValue debería ser el mismo que en el DTO.");
        assertEquals("Categoria Test", promocionValue.getCategoria(), "La categoría de la PromocionValue debería ser la misma que en el DTO.");
    }
}