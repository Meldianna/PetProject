package com.fotos.redsocial.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.fotos.redsocial.entity.relationship.FostersRelationship;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Node
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // ⭐ Solo incluir campos marcados
@ToString(onlyExplicitlyIncluded = true) // ⭐ Solo incluir campos marcados
public class Animal {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @EqualsAndHashCode.Include // ⭐ Incluir solo el ID
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

    @Relationship(type = "IS_A", direction = Relationship.Direction.OUTGOING)
    private Specie specie;

    // ⭐ EXCLUIR shelter para evitar ciclo
    @Relationship(type = "HOUSES", direction = Relationship.Direction.INCOMING)
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
}