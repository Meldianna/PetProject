package com.fotos.redsocial.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.fotos.redsocial.entity.Location;

@Repository
public interface LocationRepository extends Neo4jRepository<Location, Long>{

    Location findByName(String name);
    
}
