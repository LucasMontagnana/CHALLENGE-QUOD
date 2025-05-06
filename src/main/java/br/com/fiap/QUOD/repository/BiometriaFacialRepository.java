package br.com.fiap.QUOD.repository;

import br.com.fiap.QUOD.model.BiometriaFacial;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiometriaFacialRepository extends MongoRepository<BiometriaFacial, String> {

}