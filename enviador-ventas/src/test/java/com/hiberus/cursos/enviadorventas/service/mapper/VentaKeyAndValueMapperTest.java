package com.hiberus.cursos.enviadorventas.service.mapper;

import com.hiberus.cursos.enviadorventas.avro.VentasKey;
import com.hiberus.cursos.enviadorventas.avro.VentasValue;
import com.hiberus.cursos.enviadorventas.dto.VentasDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class VentaKeyAndValueMapperTest {

    @MockBean
    private KafkaTemplate<VentasKey, VentasValue> kafkaTemplate;
    /*
    @Test
    public void testMapeoVentasKeyVentasValue() {
        VentasDTO ventasDTO = new VentasDTO();
        ventasDTO.setCategoria("Electrónica");
        ventasDTO.setIdentificador("12345");
        ventasDTO.setCantidad(1);
        ventasDTO.setProducto("Smartphone");

        VentasKey key = new VentaKeyMapper().dtoToEntity(ventasDTO);
        VentasValue value = new VentaValueMapper().dtoToEntity(ventasDTO);

        assertNotNull(key);
        assertNotNull(value);
        assertEquals("Electrónica", key.getCategoria());
        assertEquals("Smartphone", value.getProducto());
    }


    @Test
    public void testMapeoIdentificadorVacio() {
        VentasDTO ventasDTO = new VentasDTO();
        ventasDTO.setCategoria("Electrónica");
        ventasDTO.setIdentificador("");
        ventasDTO.setCantidad(1);
        ventasDTO.setProducto("Smartphone");

        VentasKey key = new VentaKeyMapper().dtoToEntity(ventasDTO);
        VentasValue value = new VentaValueMapper().dtoToEntity(ventasDTO);

        assertNotNull(value, "El VentasKey no debería ser nulo.");
        assertEquals("", value.getIdentificador(), "El identificador debería estar vacío.");
    }

    @Test
    public void testMapeoCantidadNegativa() {

        VentasDTO ventasDTO = new VentasDTO();
        ventasDTO.setCategoria("Electrónica");
        ventasDTO.setIdentificador("12345");
        ventasDTO.setCantidad(-5);
        ventasDTO.setProducto("Smartphone");

        VentasKey key = new VentaKeyMapper().dtoToEntity(ventasDTO);
        VentasValue value = new VentaValueMapper().dtoToEntity(ventasDTO);

        assertNotNull(key, "El VentasKey no debería ser nulo.");
        assertNotNull(value, "El VentasValue no debería ser nulo.");
        assertEquals(-5, value.getCantidad(), "La cantidad negativa debería reflejarse tal cual.");
    }
     */

}