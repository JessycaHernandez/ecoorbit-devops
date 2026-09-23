package com.ecoorbit.api.repository;

import com.ecoorbit.api.model.LeituraAmbiental;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LeituraAmbientalRepository extends MongoRepository<LeituraAmbiental, String> {
    Optional<LeituraAmbiental> findByCodigo(String codigo);
    List<LeituraAmbiental> findByAreaCodigo(String areaCodigo);
    void deleteByCodigo(String codigo);
}
