package com.fotos.redsocial.entity;


import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trait {

    @Id @GeneratedValue
    private Long id;

    //private String name; 
    private String description;

    // Species -> HAS_TRAIT -> Trait (incoming)
    @Relationship(type = "HAS_TRAIT", direction = Relationship.Direction.INCOMING)
    private List<Specie> speciesWithTrait;

    // User que prefiere este trait (:User)-[:PREFERS]->(:Trait)
    @Relationship(type = "PREFERS", direction = Relationship.Direction.INCOMING)
    private List<User> preferredBy;
}
