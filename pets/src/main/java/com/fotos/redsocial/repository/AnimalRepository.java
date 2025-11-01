package com.fotos.redsocial.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.fotos.redsocial.entity.Animal;

@Repository
public interface AnimalRepository extends Neo4jRepository<Animal, Long>{

    Optional<Animal> getAnimalById(Long id);
    Set<Animal> getAllBySpecie(String specieName);
    
}
