package com.ecoorbit.api.repository;

import com.ecoorbit.api.model.AcaoFiscalizacao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AcaoFiscalizacaoRepository extends MongoRepository<AcaoFiscalizacao, String> {
    Optional<AcaoFiscalizacao> findByCodigo(String codigo);
    void deleteByCodigo(String codigo);
}
