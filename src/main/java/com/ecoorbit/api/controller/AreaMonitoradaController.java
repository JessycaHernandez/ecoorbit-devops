package com.ecoorbit.api.controller;

import com.ecoorbit.api.exception.ResourceNotFoundException;
import com.ecoorbit.api.model.AreaMonitorada;
import com.ecoorbit.api.repository.AreaMonitoradaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas-monitoradas")
public class AreaMonitoradaController {

    private final AreaMonitoradaRepository repository;

    public AreaMonitoradaController(AreaMonitoradaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<AreaMonitorada> listar() {
        return repository.findAll();
    }

    @GetMapping("/{codigo}")
    public AreaMonitorada buscarPorCodigo(@PathVariable String codigo) {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Área não encontrada: " + codigo));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AreaMonitorada criar(@Valid @RequestBody AreaMonitorada area) {
        return repository.save(area);
    }

    @PutMapping("/{codigo}")
    public AreaMonitorada atualizar(@PathVariable String codigo, @Valid @RequestBody AreaMonitorada dados) {
        AreaMonitorada existente = repository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Área não encontrada: " + codigo));
        dados.setId(existente.getId());
        dados.setCodigo(codigo);
        return repository.save(dados);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable String codigo) {
        repository.deleteByCodigo(codigo);
    }
}
