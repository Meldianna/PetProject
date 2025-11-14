package com.fotos.redsocial.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.fotos.redsocial.entity.Event;

@Repository
public interface EventRepository extends Neo4jRepository<Event, String>{
    
    @Query("""
       MATCH (s:Shelter {id: $shelterId})-[:LOCATED_AT]->(shelterLocation:Location)
                MATCH (e:Event)-[:TAKES_PLACE_IN]->(eventLocation:Location)
                MATCH path = shortestPath((shelterLocation)-[:CONNECTS*0..3]-(eventLocation))
               // DEVUELVE LAS PROPIEDADES MAPEADAS, CON LA FECHA CONVERTIDA
   RETURN e.id as id, 
          e.name as name,
          date(e.date) as date, //PARSEA LA FECHA PARA EL FORMATO ESPERADO EN JAVA, SIENDO
          e.animalEachShelter as animalEachShelter,
          e.volunteersNeeded as volunteersNeeded
        """)
    List<Event> findNearbyEventsForShelter(String shelterId);
    
}

