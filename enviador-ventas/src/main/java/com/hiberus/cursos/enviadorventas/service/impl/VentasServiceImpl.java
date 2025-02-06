package com.hiberus.cursos.enviadorventas.service.impl;


import com.hiberus.cursos.enviadorventas.avro.VentasKey;
import com.hiberus.cursos.enviadorventas.avro.VentasValue;
import com.hiberus.cursos.enviadorventas.exception.CantidadInvalidaException;
import com.hiberus.cursos.enviadorventas.exception.DatosVentaInvalidosException;
import com.hiberus.cursos.enviadorventas.exception.ProductoNoEncontradoException;
import com.hiberus.cursos.enviadorventas.model.VentasRequest;
import com.hiberus.cursos.enviadorventas.model.dto.VentasDTO;
import com.hiberus.cursos.enviadorventas.service.VentasService;
import com.hiberus.cursos.enviadorventas.service.mapper.VentaKeyMapper;
import com.hiberus.cursos.enviadorventas.service.mapper.VentaValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VentasServiceImpl implements VentasService {

    @Value("${environment.ventas-topic}")
    private String ventasTopic;

    @Autowired
    private KafkaTemplate<VentasKey, VentasValue> kafkaTemplate;


    @Override
    public void crear(VentasRequest ventasRequest) {
        try {
            validarVentasDTO(ventasRequest);
            VentasDTO ventasDTO = VentasDTO.builder()
                    .identificador(ventasRequest.getIdentificador())
                    .cantidad(ventasRequest.getCantidad())
                    .build();


            VentasKey key = new VentaKeyMapper().dtoToEntity(ventasDTO);
            VentasValue value = new VentaValueMapper().dtoToEntity(ventasDTO);

            log.debug("Enviando la venta al topic de Kafka. Topic: {}", ventasTopic);
            kafkaTemplate.send(ventasTopic, key, value);
        } catch (RuntimeException e) {
            log.error("Error al procesar la venta: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void validarVentasDTO(VentasRequest ventasRequest) {
        if (ventasRequest == null) {
            log.error("El objeto ventasDTO es nulo");
            throw new DatosVentaInvalidosException("La venta no puede ser nula");
        }

        if (ventasRequest.getIdentificador() == null || ventasRequest.getIdentificador().isBlank()) {
            log.error("El producto es nulo o vacío");
            throw new ProductoNoEncontradoException("El producto no puede ser nulo o vacío");
        }

        if (ventasRequest.getCantidad() <= 0) {
            log.error("La cantidad es inválida: {}", ventasRequest.getCantidad());
            throw new CantidadInvalidaException("La cantidad debe ser mayor a cero");
        }
    }
}
