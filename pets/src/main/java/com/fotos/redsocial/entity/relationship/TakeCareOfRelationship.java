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
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true) // ⭐ Solo incluir campos marcados
@ToString(onlyExplicitlyIncluded = true) // ⭐ Solo incluir campos marcados
@NoArgsConstructor 
@RelationshipProperties
@Data
@AllArgsConstructor
public class TakeCareOfRelationship {

    @Id @GeneratedValue
    private Long id;

    private LocalDate startDate;

    @TargetNode
    private Animal animal;
    
}
