package com.hiberus.cursos.transformadorProductos.kafka;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hiberus.cursos.enviadorproductos.avro.ProductoConImpuestoValue;
import com.hiberus.cursos.enviadorproductos.avro.ProductoKey;
import com.hiberus.cursos.enviadorproductos.avro.ProductoValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;

class TransformProductIVAServiceTest {

    @InjectMocks
    private TransformProductIVAService transformProductIVAService;

    @Mock
    private KafkaTemplate<ProductoKey, ProductoConImpuestoValue> kafkaTemplate;

    @Value("${environment.productos-impuesto-topic}")
    private String productosImpuestoTopic;

    private ProductoKey key;
    private ProductoValue value;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        key = ProductoKey.newBuilder().setCategoria("Electrónica").build();

        value = ProductoValue.newBuilder()
                .setIdentificador("123")
                .setProducto("Producto A")
                .setPrecio(100.0) // Precio base
                .setCategoria("Electrónica")
                .build();
    }

    @Test
    void testRedondear() {
        double valor = 99.876;
        double valorRedondeado = transformProductIVAService.redondear(valor);
        assertEquals(100.0, valorRedondeado);
    }

    @Test
    void testTransformar() {
        transformProductIVAService.transformar(key, value);

        double precioConIVAEsperado = 121.0;

        verify(kafkaTemplate, times(1)).send(eq(productosImpuestoTopic), eq(key), argThat(argument ->
                argument.getPrecioConImpuesto() == precioConIVAEsperado &&
                        argument.getProducto().equals("Producto A") &&
                        argument.getCategoria().equals("Electrónica")
        ));
    }
}
