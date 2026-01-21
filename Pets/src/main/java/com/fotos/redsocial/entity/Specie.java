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
public class Specie {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @ToString.Include
    private String id;

    @ToString.Include
    private String name;

    // Usuarios buscando esta especie: (:User)-[:LOOKING_FOR]->(:Species)
    @Relationship(type = "LOOKING_FOR", direction = Relationship.Direction.INCOMING)
    private List<User> lookingForByUsers;

    public Specie(String name) {
        this.name = name;
    }
}
