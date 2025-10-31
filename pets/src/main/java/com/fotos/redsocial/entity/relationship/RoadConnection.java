package com.fotos.redsocial.entity.relationship;

import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.fotos.redsocial.entity.Location;

import lombok.*;

@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor

//usada entre nodos Location
public class RoadConnection {

    @Id @GeneratedValue
    private Long id;

    private double distance;

    @TargetNode
    private Location destination;
}
