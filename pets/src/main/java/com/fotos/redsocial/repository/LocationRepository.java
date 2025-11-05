package com.fotos.redsocial.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.fotos.redsocial.entity.Location;

@Repository
public interface LocationRepository extends Neo4jRepository<Location, String>{
    @Query("MATCH (l:Location {name: $name}) RETURN l" )
    Location findByName(String name);
    
    @Query("MATCH (l:Location {id: $id}) RETURN l" )
    Location findByIdString(String id);
    
}
