// AnimalRepository.java
package com.fotos.redsocial.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.fotos.redsocial.entity.Animal;
import com.fotos.redsocial.entity.Status;

@Repository
public interface AnimalRepository extends Neo4jRepository<Animal, String> {

    Optional<Animal> findById(String id);
    Animal findByName(String name); 
    Set<Animal> getAllBySpecie(String specieName);
    List<Animal> findByStatus(Status status);
    
    @Query("""
            MATCH (a:Animal)
            WHERE a.id IN $ids
            RETURN a
            """)
    List<Animal> findAllById(List<String> ids);
}