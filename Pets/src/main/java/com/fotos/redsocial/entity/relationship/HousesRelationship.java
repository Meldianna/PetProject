package com.fotos.redsocial.entity.relationship;

import java.time.LocalDate;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fotos.redsocial.entity.Animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@RelationshipProperties
@JsonIgnoreProperties({"shelter", "animal"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) //Excluye @TargetNode (animal) para evitar bucles infinitos
@ToString(onlyExplicitlyIncluded = true)
public class HousesRelationship {

    @Id 
    @GeneratedValue
    @EqualsAndHashCode.Include 
    @ToString.Include
    private String id;

    @EqualsAndHashCode.Include
    @ToString.Include
    private LocalDate since;

    
    @TargetNode
    private Animal animal;

    public HousesRelationship(LocalDate since, Animal animal){
        this.since = since;
        this.animal = animal;
    }
}