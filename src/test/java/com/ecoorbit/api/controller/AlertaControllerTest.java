package com.ecoorbit.api.controller;

import com.ecoorbit.api.model.Alerta;
import com.ecoorbit.api.repository.AlertaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlertaController.class)
class AlertaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertaRepository repository;

    @Test
    void deveListarAlertasPorArea() throws Exception {
        Alerta alerta = new Alerta();
        alerta.setCodigo("ALT001");
        alerta.setAreaCodigo("AREA001");
        alerta.setTipo("desmatamento");
        alerta.setNivelRisco("alto");

        when(repository.findByAreaCodigo("AREA001")).thenReturn(List.of(alerta));

        mockMvc.perform(get("/api/alertas").param("areaCodigo", "AREA001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("ALT001"))
                .andExpect(jsonPath("$[0].tipo").value("desmatamento"));
    }
}
