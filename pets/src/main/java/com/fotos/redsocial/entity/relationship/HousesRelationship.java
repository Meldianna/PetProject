package com.fotos.redsocial.entity.relationship;

import java.time.LocalDate;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.fotos.redsocial.entity.Animal;

import lombok.*;

@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // ⭐ Solo incluir campos marcados
@ToString(onlyExplicitlyIncluded = true)
public class HousesRelationship {

    @Id 
    @GeneratedValue
    @EqualsAndHashCode.Include // ⭐ Solo el ID
    @ToString.Include
    private Long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    private LocalDate since;

    // ⭐ EXCLUIR animal para evitar ciclo
    @TargetNode
    private Animal animal;

    public HousesRelationship(LocalDate since, Animal animal){
        this.since = since;
        this.animal = animal;
    }
}