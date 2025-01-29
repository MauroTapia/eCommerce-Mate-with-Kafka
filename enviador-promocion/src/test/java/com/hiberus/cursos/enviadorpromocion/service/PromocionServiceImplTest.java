package com.hiberus.cursos.enviadorpromocion.service;

import com.hiberus.cursos.enviadorpromocion.avro.PromocionKey;
import com.hiberus.cursos.enviadorpromocion.avro.PromocionValue;
import com.hiberus.cursos.enviadorpromocion.dto.PromocionDTO;
import com.hiberus.cursos.enviadorpromocion.exception.CategoriaNoEncontradoException;
import com.hiberus.cursos.enviadorpromocion.exception.DatosPromocionInvalidosException;
import com.hiberus.cursos.enviadorpromocion.exception.DescuentoInvalidaException;
import com.hiberus.cursos.enviadorpromocion.exception.PromocionNoEncontradoException;
import com.hiberus.cursos.enviadorpromocion.service.impl.PromocionServiceImpl;
import com.hiberus.cursos.enviadorpromocion.service.mapper.PromocionKeyMapper;
import com.hiberus.cursos.enviadorpromocion.service.mapper.PromocionValueMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PromocionServiceImplTest {

    @InjectMocks
    private PromocionServiceImpl promocionService;

    @Mock
    private KafkaTemplate<PromocionKey, PromocionValue> kafkaTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(promocionService, "promocionTopic", "test-topic");
    }

    @Test
    void testCrearEnvioExitoso() {
        PromocionDTO promocionDTO = new PromocionDTO("Master card week", 10.0, "cyber monday");

        PromocionKey key = new PromocionKeyMapper().dtoToEntity(promocionDTO);
        PromocionValue value = new PromocionValueMapper().dtoToEntity(promocionDTO);

        promocionService.crear(promocionDTO);

        verify(kafkaTemplate, times(1)).send(eq("test-topic"), eq(key), eq(value));
    }

    @Test
    void testCrearPromocionDTONulo() {
        DatosPromocionInvalidosException exception = assertThrows(DatosPromocionInvalidosException.class, () -> {
            promocionService.crear(null);
        });

        assertEquals("La promocion no puede ser nulo", exception.getMessage());
    }

    @Test
    void testCrearCategoriaNula() {
        PromocionDTO promocionDTO = new PromocionDTO("Master card week", 10.0, null);

        CategoriaNoEncontradoException exception = assertThrows(CategoriaNoEncontradoException.class, () -> {
            promocionService.crear(promocionDTO);
        });

        assertEquals("La categoría no puede ser nula o vacía", exception.getMessage());
    }

    @Test
    void testCrearNombreNulo() {
        PromocionDTO promocionDTO = new PromocionDTO(null, 10.0, "cyber monday");

        PromocionNoEncontradoException exception = assertThrows(PromocionNoEncontradoException.class, () -> {
            promocionService.crear(promocionDTO);
        });

        assertEquals("El nombre de la promocion no puede ser nula o vacía", exception.getMessage());
    }

    @Test
    void testCrearDescuentoInvalido() {
        PromocionDTO promocionDTO = new PromocionDTO("Master card week", -10.0, "cyber monday");

        DescuentoInvalidaException exception = assertThrows(DescuentoInvalidaException.class, () -> {
            promocionService.crear(promocionDTO);
        });

        assertEquals("La cantidad debe ser igual o mayor a cero", exception.getMessage());
    }
}
