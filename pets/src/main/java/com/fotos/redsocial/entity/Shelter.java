package com.fotos.redsocial.entity;



import org.springframework.data.neo4j.core.schema.*;

import lombok.*;

import java.util.List;

import com.fotos.redsocial.entity.relationship.HousesRelationship;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shelter {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String address;

    // Shelter -> Location (LOCATED_IN)
    @Relationship(type = "LOCATED_IN", direction = Relationship.Direction.OUTGOING)
    private Location locatedIn;

    // Shelter -> Event (PARTICIPATES_IN)
    @Relationship(type = "PARTICIPATES_IN", direction = Relationship.Direction.OUTGOING)
    private List<Event> participatesIn;

    // Shelter -> Animal (HOUSES)
    @Relationship(type = "HOUSES", direction = Relationship.Direction.OUTGOING)
    private List<HousesRelationship> houses;

    // Usuarios que trabajan / administran / siguen el shelter
    // (:User)-[:WORKS_FOR|:ADMINISTERS|:FOLLOWS]->(:Shelter)
    @Relationship(type = "WORKS_FOR", direction = Relationship.Direction.INCOMING)
    private List<User> workers;

    @Relationship(type = "ADMINISTERS", direction = Relationship.Direction.INCOMING)
    private List<User> administers; // usuarios que administran

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.INCOMING)
    private List<User> followers;
}
