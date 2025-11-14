// FostersRelationship.java
package com.fotos.redsocial.entity.relationship;

import java.time.LocalDate;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

import org.springframework.data.neo4j.core.schema.TargetNode;

import com.fotos.redsocial.entity.Animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true) 
@ToString(onlyExplicitlyIncluded = true) 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FostersRelationship {
    
    @Id 
    @GeneratedValue
    private String id;

    private LocalDate fosteredSince;

    @TargetNode
    private Animal fosteredAnimal;
    

    public FostersRelationship(LocalDate fosteredSince, Animal fosteredAnimal) {
        this.fosteredSince = fosteredSince;
        this.fosteredAnimal = fosteredAnimal;
    }
}