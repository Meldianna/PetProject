package com.fotos.redsocial.entity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Event {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @ToString.Include
    private String id;

    @ToString.Include
    private String name;

    @ToString.Include
    private LocalDate date;

    private int animalEachShelter; // cambio acá
    private int volunteersNeeded; // cambio acá

    // Event -> Location (TAKES_PLACE_IN)
    @Relationship(type = "TAKES_PLACE_IN", direction = Relationship.Direction.OUTGOING)
    private Location location;

    // Event -> User (ORGANIZED_BY)
    @Relationship(type = "ORGANIZED_BY", direction = Relationship.Direction.OUTGOING)
    private User organizer;

    // (:User)-[:ENROLLED_IN]->(:Event) => Usuarios del evento
    @Relationship(type = "ENROLLED_IN", direction = Relationship.Direction.INCOMING)
    private List<User> enrolledUsers;

    // Shelters que participan del evento (Shelter -> PARTICIPATES_IN -> Event)
    @Relationship(type = "PARTICIPATES_IN", direction = Relationship.Direction.INCOMING)
    private List<Shelter> participatingShelters;

}
