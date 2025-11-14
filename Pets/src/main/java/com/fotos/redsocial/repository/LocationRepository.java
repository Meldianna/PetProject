package com.fotos.redsocial.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.fotos.redsocial.entity.Location;

public interface LocationRepository extends Neo4jRepository<Location, String> {
    
    @Query("MATCH (l:Location)<-[:LOCATED_AT]-(s:Shelter) RETURN DISTINCT l")
    List<Location> findLocationsWithShelters();

    @Query("""
        MATCH (u:User {email: $userEmail})-[:TAKES_CARE_OF]->(a:Animal)
        MATCH (a)<-[:HOUSES]-(s:Shelter)
        MATCH (s)-[:LOCATED_AT]->(l:Location)
        RETURN DISTINCT l
        """)
    List<Location> findTargetLocationsForUser(@Param("userEmail") String userEmail);

    @Query("""
            MATCH (l1:Location{id: $currentId})-[r:CONNECTS]->(l2:Location{id: $startdId})
            RETURN r.distance
            """)
    Double getDistanceBetween(@Param("currentId") String currentId, @Param("startdId") String startdId);
}
