package com.ecoorbit.api.repository;

import com.ecoorbit.api.model.Alerta;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AlertaRepository extends MongoRepository<Alerta, String> {
    Optional<Alerta> findByCodigo(String codigo);
    List<Alerta> findByAreaCodigo(String areaCodigo);
    void deleteByCodigo(String codigo);
}
