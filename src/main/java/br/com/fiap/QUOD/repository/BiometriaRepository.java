package br.com.fiap.QUOD.repository;

import br.com.fiap.QUOD.model.Biometria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiometriaRepository  extends MongoRepository<Biometria, String> {

}