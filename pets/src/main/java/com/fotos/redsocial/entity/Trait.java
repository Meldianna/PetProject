package com.fotos.redsocial.entity;


import org.springframework.data.neo4j.core.schema.*;
import lombok.*;
import java.util.List;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trait {

    @Id @GeneratedValue
    private Long id;

    private String name; 
    private String description;

    // Species -> HAS_TRAIT -> Trait (incoming)
    @Relationship(type = "HAS_TRAIT", direction = Relationship.Direction.INCOMING)
    private List<Species> speciesWithTrait;

    // User que prefiere este trait (:User)-[:PREFERS]->(:Trait)
    @Relationship(type = "PREFERS", direction = Relationship.Direction.INCOMING)
    private List<User> preferredBy;
}
