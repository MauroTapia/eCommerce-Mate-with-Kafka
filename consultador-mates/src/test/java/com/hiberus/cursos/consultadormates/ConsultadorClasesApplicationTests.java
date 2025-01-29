package com.hiberus.cursos.consultadormates;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConsultadorClasesApplicationTests {

    @Test
    void contextLoads() {
        assertNotNull(ConsultadorClasesApplication.class);
    }

    @Test
    void shouldInvokeMain() {
        try (MockedStatic<SpringApplication> mockedStatic = Mockito.mockStatic(SpringApplication.class)) {
            ConsultadorClasesApplication.main(new String[]{});

            mockedStatic.verify(() ->
                    SpringApplication.run(ConsultadorClasesApplication.class, new String[]{})
            );
        }
    }
}