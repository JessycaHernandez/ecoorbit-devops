package com.ecoorbit.api.repository;

import com.ecoorbit.api.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByCodigo(String codigo);
    void deleteByCodigo(String codigo);
}
