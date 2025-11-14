package com.fotos.redsocial.entity.relationship;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.fotos.redsocial.entity.Trait;

import lombok.*;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true) 
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor 
@RelationshipProperties
@Data
@AllArgsConstructor
//usada entre nodos User y Trait
public class PrefersRelationship {
    
    @Id @GeneratedValue
    private String id;

    @TargetNode
    private Trait traitNode;
}
