package com.fotos.redsocial.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.fotos.redsocial.entity.Shelter;
import com.fotos.redsocial.service.GraphService.MSTEdgeData;

@Repository
public interface ShelterRepository extends Neo4jRepository<Shelter, String>{

    Shelter findByName(String name);
    @Query("""
    MATCH (s:Shelter)-[:LOCATED_IN]->(l:Location)
    MATCH (s2:Shelter)-[:LOCATED_IN]->(l2:Location)
    WHERE s <> s2
    AND id(s) < id(s2) // evita duplicar pares A→B y B→A
    OPTIONAL MATCH path = shortestPath((l)-[:ROAD_TO*1..10]-(l2))
    WITH s, s2, path
    WHERE path IS NOT NULL
    WITH s, s2,
        reduce(total=0, r IN relationships(path) |
            total + coalesce(r.distance, 0.0)
        ) AS totalDistance
    WHERE totalDistance IS NOT NULL AND totalDistance > 0
    RETURN s.id AS sourceId,
        s.name AS sourceName,
        s2.id AS destinationId,
        s2.name AS destinationName,
        totalDistance AS distance
    ORDER BY totalDistance
    """)
    List<MSTEdgeData> getShelterConnections();
    
}
