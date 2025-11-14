package com.fotos.redsocial.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fotos.redsocial.entity.relationship.FostersRelationship;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Node("Animal")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Animal {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;

    @EqualsAndHashCode.Include
    @ToString.Include
    private String name;
    
    @ToString.Include
    private int age;
    
    @ToString.Include
    private String gender;
    
    @ToString.Include
    private Status status;

@Relationship(type = "IS_OF_SPECIES", direction = Relationship.Direction.OUTGOING)
    private Specie specie;

    @JsonBackReference
    @Relationship(type = "HOUSED_IN")
    private Shelter shelter;

    @Relationship(type = "TAKE_CARE_OF", direction = Relationship.Direction.INCOMING)
    private List<User> caretakers;

    @Relationship(type = "ADOPTS", direction = Relationship.Direction.INCOMING)
    private User adopter;

    @Relationship(type = "FOSTERS", direction = Relationship.Direction.INCOMING)
    private List<FostersRelationship> fosterers;    

    @Relationship(type = "HAS_TRAIT", direction = Relationship.Direction.OUTGOING)
    private List<Trait> traits;

    public Animal(String name, int age, String gender, Specie specie, Shelter shelter){
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.specie = specie;
        this.shelter = shelter;
        this.status = Status.AVAILABLE;
        
        this.caretakers = new ArrayList<>();
        this.traits = new ArrayList<>();
        this.adopter = null;
        this.fosterers = null;
    }

    public Specie getSpecie() {
        return specie;
    }

    public void setSpecie(Specie specie) {
        this.specie = specie;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    public List<User> getCaretakers() {
        return caretakers;
    }

    public void setCaretakers(List<User> caretakers) {
        this.caretakers = caretakers;
    }

    public User getAdopter() {
        return adopter;
    }

    public void setAdopter(User adopter) {
        this.adopter = adopter;
    }

    public List<FostersRelationship> getFosterers() {
        return fosterers;
    }

    public void setFosterers(List<FostersRelationship> fosterers) {
        this.fosterers = fosterers;
    }

    public List<Trait> getTraits() {
        return traits;
    }

    public void setTraits(List<Trait> traits) {
        this.traits = traits;
    }
}