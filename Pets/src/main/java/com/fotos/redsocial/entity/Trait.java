package com.fotos.redsocial.entity;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Trait {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @ToString.Include
    private String id;

    @ToString.Include
    private String description;

    // Species -> HAS_TRAIT -> Trait (incoming)
    @Relationship(type = "HAS_TRAIT", direction = Relationship.Direction.INCOMING)
    private List<Specie> speciesWithTrait;

    // User que prefiere este trait (:User)-[:PREFERS]->(:Trait)
    @Relationship(type = "PREFERS", direction = Relationship.Direction.INCOMING)
    private List<User> preferredBy;

    public Trait(String desc) {
        this.description = desc;

    }
}
