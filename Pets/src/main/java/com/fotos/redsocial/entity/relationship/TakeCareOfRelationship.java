package com.fotos.redsocial.entity.relationship;

import java.time.LocalDate;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.fotos.redsocial.entity.Animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(onlyExplicitlyIncluded = true) 
@NoArgsConstructor 
@RelationshipProperties
@Data
@AllArgsConstructor
public class TakeCareOfRelationship {

    @Id @GeneratedValue
    @ToString.Include
    private String id;

    @ToString.Include
    private LocalDate startDate;

    @TargetNode
    private Animal animal;
    
}
