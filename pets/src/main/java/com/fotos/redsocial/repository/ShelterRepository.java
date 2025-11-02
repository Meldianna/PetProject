package com.fotos.redsocial.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.fotos.redsocial.entity.Shelter;

@Repository
public interface ShelterRepository extends Neo4jRepository<Shelter, Long>{
    
}
