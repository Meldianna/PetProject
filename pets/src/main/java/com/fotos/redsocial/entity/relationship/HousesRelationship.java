package com.fotos.redsocial.entity.relationship;

import java.lang.annotation.Target;
import java.time.LocalDate;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.fotos.redsocial.entity.Animal;

import lombok.*;

@RelationshipProperties
@Data
@AllArgsConstructor

//usada para conocer hace cuánto está el perro en el refugio
//podemos darle un orden de prioridad según si están más tiempo ahí

public class HousesRelationship {

    @Id @GeneratedValue
    private Long id;

    private LocalDate since;

    @TargetNode
    private Animal animal;

    
}
