package com.ecoorbit.api.controller;

import com.ecoorbit.api.model.AreaMonitorada;
import com.ecoorbit.api.repository.AreaMonitoradaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AreaMonitoradaController.class)
class AreaMonitoradaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AreaMonitoradaRepository repository;

    @Test
    void deveListarAreasMonitoradas() throws Exception {
        AreaMonitorada area = new AreaMonitorada();
        area.setCodigo("AREA001");
        area.setNome("Reserva Amazônica Norte");
        area.setBioma("Amazônia");

        when(repository.findAll()).thenReturn(List.of(area));

        mockMvc.perform(get("/api/areas-monitoradas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("AREA001"))
                .andExpect(jsonPath("$[0].bioma").value("Amazônia"));
    }

    @Test
    void deveRetornar404QuandoAreaNaoExiste() throws Exception {
        when(repository.findByCodigo("AREA999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/areas-monitoradas/AREA999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveCriarNovaAreaMonitorada() throws Exception {
        AreaMonitorada novaArea = new AreaMonitorada();
        novaArea.setCodigo("AREA020");
        novaArea.setNome("Cerrado Teste");
        novaArea.setNivelRisco("medio");

        when(repository.save(org.mockito.ArgumentMatchers.any(AreaMonitorada.class))).thenReturn(novaArea);

        mockMvc.perform(post("/api/areas-monitoradas")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(novaArea)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value("AREA020"));
    }
}
