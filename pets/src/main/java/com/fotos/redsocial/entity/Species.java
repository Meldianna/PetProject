package com.fotos.redsocial.entity;



import org.springframework.data.neo4j.core.schema.*;
import lombok.*;
import java.util.List;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Species {

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
