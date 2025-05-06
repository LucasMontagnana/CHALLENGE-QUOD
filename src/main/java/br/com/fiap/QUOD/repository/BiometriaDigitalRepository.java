package br.com.fiap.QUOD.repository;

import br.com.fiap.QUOD.model.BiometriaDigital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiometriaDigitalRepository extends MongoRepository<BiometriaDigital, String> {
}
