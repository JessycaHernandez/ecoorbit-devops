package com.ecoorbit.api.controller;

import com.ecoorbit.api.exception.ResourceNotFoundException;
import com.ecoorbit.api.model.Usuario;
import com.ecoorbit.api.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Usuario> listar() {
        return repository.findAll();
    }

    @GetMapping("/{codigo}")
    public Usuario buscarPorCodigo(@PathVariable String codigo) {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + codigo));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario criar(@Valid @RequestBody Usuario usuario) {
        return repository.save(usuario);
    }

    @PutMapping("/{codigo}")
    public Usuario atualizar(@PathVariable String codigo, @Valid @RequestBody Usuario dados) {
        Usuario existente = repository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + codigo));
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
