package com.ecoorbit.api.controller;

import com.ecoorbit.api.exception.ResourceNotFoundException;
import com.ecoorbit.api.model.AcaoFiscalizacao;
import com.ecoorbit.api.repository.AcaoFiscalizacaoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acoes-fiscalizacao")
public class AcaoFiscalizacaoController {

    private final AcaoFiscalizacaoRepository repository;

    public AcaoFiscalizacaoController(AcaoFiscalizacaoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<AcaoFiscalizacao> listar() {
        return repository.findAll();
    }

    @GetMapping("/{codigo}")
    public AcaoFiscalizacao buscarPorCodigo(@PathVariable String codigo) {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Ação não encontrada: " + codigo));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AcaoFiscalizacao criar(@Valid @RequestBody AcaoFiscalizacao acao) {
        return repository.save(acao);
    }

    @PutMapping("/{codigo}")
    public AcaoFiscalizacao atualizar(@PathVariable String codigo, @Valid @RequestBody AcaoFiscalizacao dados) {
        AcaoFiscalizacao existente = repository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Ação não encontrada: " + codigo));
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
