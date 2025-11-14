package com.fotos.redsocial.entity.relationship;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.fotos.redsocial.entity.Animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true) 
@ToString(onlyExplicitlyIncluded = true) 
@NoArgsConstructor 
@RelationshipProperties
@Data
@AllArgsConstructor
public class AdoptsRelationship {

    @Id @GeneratedValue
    private String id;
    
    private LocalDate adoptedIn;

    @TargetNode
    private Animal adoptedAnimal;
}
