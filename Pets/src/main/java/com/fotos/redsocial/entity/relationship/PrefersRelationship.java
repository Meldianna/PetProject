package com.fotos.redsocial.entity.relationship;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.fotos.redsocial.entity.Trait;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@RelationshipProperties
@Data
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true) // Excluye @TargetNode para evitar bucles
// usada entre nodos User y Trait
public class PrefersRelationship {

    @Id
    @GeneratedValue
    @ToString.Include
    private String id;

    @TargetNode
    private Trait traitNode;
}
