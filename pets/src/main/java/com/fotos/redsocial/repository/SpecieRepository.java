package com.fotos.redsocial.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.fotos.redsocial.entity.Specie;

@Repository
public interface SpecieRepository extends Neo4jRepository<Specie, String>{
    Specie findByName(String name);

    
} 
