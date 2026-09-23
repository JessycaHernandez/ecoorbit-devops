package com.ecoorbit.api.controller;

import com.ecoorbit.api.exception.ResourceNotFoundException;
import com.ecoorbit.api.model.Alerta;
import com.ecoorbit.api.repository.AlertaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final AlertaRepository repository;

    public AlertaController(AlertaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Alerta> listar(@RequestParam(required = false) String areaCodigo) {
        if (areaCodigo != null) {
            return repository.findByAreaCodigo(areaCodigo);
        }
        return repository.findAll();
    }

    @GetMapping("/{codigo}")
    public Alerta buscarPorCodigo(@PathVariable String codigo) {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado: " + codigo));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Alerta criar(@Valid @RequestBody Alerta alerta) {
        return repository.save(alerta);
    }

    @PutMapping("/{codigo}")
    public Alerta atualizar(@PathVariable String codigo, @Valid @RequestBody Alerta dados) {
        Alerta existente = repository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado: " + codigo));
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
