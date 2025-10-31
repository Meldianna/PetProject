package com.fotos.redsocial.entity;



import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.fotos.redsocial.entity.relationship.AdoptsRelationship;
import com.fotos.redsocial.entity.relationship.FostersRelationship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import com.fotos.redsocial.entity.relationship.PrefersRelationship;


@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private String phone;

    // Relaciones hacia Location, Shelter, Animal, Event, Trait, Species

    // User -> Location (LIVES_IN)
    @Relationship(type = "LIVES_IN", direction = Relationship.Direction.OUTGOING)
    private Location livesIn;

    // User -> Shelter (WORKS_FOR, ADMINISTERS, FOLLOWS)
    @Relationship(type = "WORKS_FOR", direction = Relationship.Direction.OUTGOING)
    private Shelter worksFor;

    @Relationship(type = "ADMINISTERS", direction = Relationship.Direction.OUTGOING)
    private List<Shelter> administers;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private List<Shelter> follows;

    // User -> Event (ENROLLED_IN)  : (:User)-[:ENROLLED_IN]->(:Event)
    @Relationship(type = "ENROLLED_IN", direction = Relationship.Direction.OUTGOING)
    private List<Event> enrolledEvents;

    // User -> Event (ORGANIZER) 
    @Relationship(type = "ORGANIZED_BY", direction = Relationship.Direction.INCOMING)
    private List<Event> organizedEvents;

    // User -> Animal (ADOPTS, TAKE_CARE_OF)
    @Relationship(type = "ADOPTS", direction = Relationship.Direction.OUTGOING)
    private List<AdoptsRelationship> adoptedAnimals;

    @Relationship(type = "TAKE_CARE_OF", direction = Relationship.Direction.OUTGOING)
    private List<Animal> caringFor;

    @Relationship(type = "FOSTERS", direction = Relationship.Direction.OUTGOING)
    private List<FostersRelationship> fostering;


    // User -> Species (LOOKING_FOR)
    @Relationship(type = "LOOKING_FOR", direction = Relationship.Direction.OUTGOING)
    private List<Species> lookingFor;

    // User -> Trait (PREFERS)
    @Relationship(type = "PREFERS", direction = Relationship.Direction.OUTGOING)
    private List<Trait> preferredTraits;
    //se puede usar el PreferRelationship si queremos agregar una propiedad como "relevancia".
}
