package com.ecoorbit.api.repository;

import com.ecoorbit.api.model.AreaMonitorada;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AreaMonitoradaRepository extends MongoRepository<AreaMonitorada, String> {
    Optional<AreaMonitorada> findByCodigo(String codigo);
    void deleteByCodigo(String codigo);
}
