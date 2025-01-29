package com.hiberus.cursos.enviadorpromocion.service.impl;

import com.hiberus.cursos.enviadorpromocion.avro.PromocionKey;
import com.hiberus.cursos.enviadorpromocion.avro.PromocionValue;
import com.hiberus.cursos.enviadorpromocion.dto.PromocionDTO;
import com.hiberus.cursos.enviadorpromocion.exception.CategoriaNoEncontradoException;
import com.hiberus.cursos.enviadorpromocion.exception.DatosPromocionInvalidosException;
import com.hiberus.cursos.enviadorpromocion.exception.DescuentoInvalidaException;
import com.hiberus.cursos.enviadorpromocion.exception.PromocionNoEncontradoException;
import com.hiberus.cursos.enviadorpromocion.service.PromocionService;
import com.hiberus.cursos.enviadorpromocion.service.mapper.PromocionKeyMapper;
import com.hiberus.cursos.enviadorpromocion.service.mapper.PromocionValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PromocionServiceImpl implements PromocionService {

    public void setPromocionTopic(String promocionTopic) {
        this.promocionTopic = promocionTopic;
    }

    @Value("${environment.promocion-topic}")
    private String promocionTopic;

    @Autowired
    private KafkaTemplate<PromocionKey, PromocionValue> kafkaTemplate;

    @Override
    public void crear(PromocionDTO promocionDTO) {
        try {
            validarPromocionDTO(promocionDTO);

            PromocionKey key = new PromocionKeyMapper().dtoToEntity(promocionDTO);
            PromocionValue value = new PromocionValueMapper().dtoToEntity(promocionDTO);

            log.debug("Enviando el profesor al topic de kafka");
            kafkaTemplate.send(promocionTopic, key, value);
        } catch (RuntimeException e) {
            log.error("Error al procesar la venta: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void validarPromocionDTO(PromocionDTO promocionDTO) {
        if (promocionDTO == null) {
            log.error("El objeto promocionDTO es nulo");
            throw new DatosPromocionInvalidosException("La promocion no puede ser nulo");
        }

        if (promocionDTO.getCategoria() == null || promocionDTO.getCategoria().isBlank()) {
            log.error("La categoría es nula o vacía");
            throw new CategoriaNoEncontradoException("La categoría no puede ser nula o vacía");
        }

        if (promocionDTO.getNombre() == null || promocionDTO.getNombre().isBlank()) {
            log.error("La promocion es nula o vacía");
            throw new PromocionNoEncontradoException("El nombre de la promocion no puede ser nula o vacía");
        }

        if (promocionDTO.getDescuento() < 0) {
            log.error("El descuento es inválida: {}", promocionDTO.getDescuento());
            throw new DescuentoInvalidaException("La cantidad debe ser igual o mayor a cero");
        }
    }

}
