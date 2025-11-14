package com.fotos.redsocial.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fotos.redsocial.entity.Shelter;

@Repository
public interface ShelterRepository extends Neo4jRepository<Shelter, String> {
    Shelter findByName(String name);
    
    @Query("MATCH (s:Shelter) RETURN s ORDER BY s.capacity")
    List<Shelter> findAllOrderByCapacity();
    
    @Query("""
            MATCH (s:Shelter)-[located:LOCATED_AT]->(l:Location)
            MATCH (s2:Shelter)-[:LOCATED_AT]->(l2:Location)
            WHERE s <> s2 AND id(s) < id(s2)
            OPTIONAL MATCH path = shortestPath((l)-[:CONNECTS*]-(l2))
            WHERE path IS NOT NULL
            RETURN 
                id(s) as sourceId,
                s.name as sourceName,
                id(s2) as destinationId,
                s2.name as destinationName,
                reduce(dist = 0.0, r IN relationships(path) | dist + r.distance) as distance
            ORDER BY distance
            """)
    List<MSTEdgeData> getShelterConnections();

    @Query("MATCH (s:Shelter) RETURN count(s)")
    long count();

    /**
     * CONSULTA CLAVE 1 (Para el Algoritmo DP):
     * Cuenta eficientemente cuántos voluntarios (User) activos 
     * tiene un refugio (Shelter).
     */
    @Query("MATCH (s:Shelter {id: $shelterId})<-[:WORKS_IN]-(u:User) RETURN count(u)")
    int countVolunteersForShelter(@Param("shelterId") String shelterId);

    /**
     * CONSULTA CLAVE 2 (Para el Algoritmo DP):
     * Cuenta eficientemente cuántos animales (Animal) alberga 
     * actualmente un refugio (Shelter).
     */
    @Query("MATCH (s:Shelter {id: $shelterId})-[:HOUSES]->(a:Animal) RETURN count(a)")
    int countAnimalsForShelter(@Param("shelterId") String shelterId);
}
