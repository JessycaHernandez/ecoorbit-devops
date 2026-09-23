package com.ecoorbit.api.controller;

import com.ecoorbit.api.exception.ResourceNotFoundException;
import com.ecoorbit.api.model.LeituraAmbiental;
import com.ecoorbit.api.repository.LeituraAmbientalRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leituras-ambientais")
public class LeituraAmbientalController {

    private final LeituraAmbientalRepository repository;

    public LeituraAmbientalController(LeituraAmbientalRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<LeituraAmbiental> listar(@RequestParam(required = false) String areaCodigo) {
        if (areaCodigo != null) {
            return repository.findByAreaCodigo(areaCodigo);
        }
        return repository.findAll();
    }

    @GetMapping("/{codigo}")
    public LeituraAmbiental buscarPorCodigo(@PathVariable String codigo) {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Leitura não encontrada: " + codigo));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeituraAmbiental criar(@Valid @RequestBody LeituraAmbiental leitura) {
        return repository.save(leitura);
    }

    @PutMapping("/{codigo}")
    public LeituraAmbiental atualizar(@PathVariable String codigo, @Valid @RequestBody LeituraAmbiental dados) {
        LeituraAmbiental existente = repository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Leitura não encontrada: " + codigo));
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
