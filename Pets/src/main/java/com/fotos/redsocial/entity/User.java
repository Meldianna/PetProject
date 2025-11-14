package com.fotos.redsocial.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.fotos.redsocial.entity.relationship.AdoptsRelationship;
import com.fotos.redsocial.entity.relationship.FostersRelationship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Node("User")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // ⭐ Solo incluir campos marcados
@ToString(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @EqualsAndHashCode.Include // ⭐ Solo el ID y email
    @ToString.Include
    private String id;

    @ToString.Include
    private String name;
    
    @EqualsAndHashCode.Include
    @ToString.Include
    private String email;
    
    @ToString.Include
    private String phone;

    // Todas las relaciones se excluyen automáticamente
    @Relationship(type = "LIVES_IN", direction = Relationship.Direction.OUTGOING)
    private Location livesIn;

    @Relationship(type = "WORKS_IN", direction = Relationship.Direction.OUTGOING)
    private Shelter worksFor;

    @Relationship(type = "ADMINISTERS", direction = Relationship.Direction.OUTGOING)
    private List<Shelter> administers;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private List<Shelter> follows;

    @Relationship(type = "ENROLLED_IN", direction = Relationship.Direction.OUTGOING)
    private List<Event> enrolledEvents;

    @Relationship(type = "ORGANIZED_BY", direction = Relationship.Direction.INCOMING)
    private List<Event> organizedEvents;

    @Relationship(type = "ADOPTS", direction = Relationship.Direction.OUTGOING)
    private List<AdoptsRelationship> adoptedAnimals;

    @Relationship(type = "TAKES_CARE_OF", direction = Relationship.Direction.OUTGOING)
    private List<Animal> caringFor;

    @Relationship(type = "FOSTERS", direction = Relationship.Direction.OUTGOING)
    private List<FostersRelationship> fostering;

    @Relationship(type = "LOOKS_FOR", direction = Relationship.Direction.OUTGOING)
    private List<Specie> lookingFor;

    @Relationship(type = "PREFERS", direction = Relationship.Direction.OUTGOING)
    private List<Trait> preferredTraits;
    
    @Relationship(type = "FRIENDS_WITH", direction = Relationship.Direction.OUTGOING)
    private Set<User> friendship;

    // @Relationship(type = "PREFERS", direction = Relationship.Direction.OUTGOING)
    // private List<Specie> preferredSpecies;

    public User(String name, String email, String phone, Location location){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.livesIn = location;
        
        this.administers = new ArrayList<>();
        this.follows = new ArrayList<>();
        this.enrolledEvents = new ArrayList<>();
        this.organizedEvents = new ArrayList<>();
        this.adoptedAnimals = new ArrayList<>();
        this.caringFor = new ArrayList<>();
        this.fostering = new ArrayList<>();
        this.lookingFor = new ArrayList<>();
        this.preferredTraits = new ArrayList<>();
        this.friendship = new HashSet<>();
        //this.preferredSpecies = new ArrayList<>();
    }
    
    public String getId() {
        return id;
    }

    // public List<Specie> getPreferredSpecies() {
    //     return preferredSpecies;
    // }

    public List<Trait> getPreferredTraits() {
        return preferredTraits;
    }

    // public boolean hasPreference(Specie species) {
    //     return getPreferredSpecies().contains(species);
    // }
}