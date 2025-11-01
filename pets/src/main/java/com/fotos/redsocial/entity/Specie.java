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
public class Specie {

    @Id @GeneratedValue
    private Long id;

    private String name;

    // // Species -> Trait (HAS_TRAIT)
    // @Relationship(type = "HAS_TRAIT", direction = Relationship.Direction.OUTGOING)
    // private List<Trait> traits;

    // Usuarios buscando esta especie: (:User)-[:LOOKING_FOR]->(:Species)
    @Relationship(type = "LOOKING_FOR", direction = Relationship.Direction.INCOMING)
    private List<User> lookingForByUsers;
}
