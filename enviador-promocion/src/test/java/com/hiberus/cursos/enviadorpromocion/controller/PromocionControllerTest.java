
package com.hiberus.cursos.enviadorpromocion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiberus.cursos.enviadorpromocion.dto.PromocionDTO;
import com.hiberus.cursos.enviadorpromocion.exception.CategoriaNoEncontradoException;
import com.hiberus.cursos.enviadorpromocion.exception.GlobalExceptionHandler;
import com.hiberus.cursos.enviadorpromocion.service.impl.PromocionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(MockitoExtension.class)
public class PromocionControllerTest {

    @InjectMocks
    private PromocionControllerImpl promocionController;

    @Mock
    private PromocionServiceImpl promocionService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(promocionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private ObjectMapper objectMapper = new ObjectMapper();

   @Test
    public void testCrearPromocionExitoso() throws Exception {
        PromocionDTO promocionDTO = new PromocionDTO("Promocion A", 100.0, "Categoria Test");

        doNothing().when(promocionService).crear(any(PromocionDTO.class));

       mockMvc.perform(post("/promocion")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(promocionDTO)))
               .andExpect(status().isCreated());

       verify(promocionService, times(1)).crear(any(PromocionDTO.class));

   }

    @Test
    public void testCrearPromocionConDatosInvalidos() throws Exception {
        PromocionDTO promocionDTOInvalido = new PromocionDTO();
        promocionDTOInvalido.setNombre("cyber monday");
        promocionDTOInvalido.setDescuento(10.0);
        promocionDTOInvalido.setCategoria("");

        doThrow(new CategoriaNoEncontradoException("La categoría no puede ser nula o vacía"))
                .when(promocionService).crear(any(PromocionDTO.class));

        mockMvc.perform(post("/promocion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoria\":\"Categoria Test\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La categoría no puede ser nula o vacía"));

        verify(promocionService, times(1)).crear(any(PromocionDTO.class));

    }

}
