package com.fotos.redsocial.entity.relationship;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.fotos.redsocial.entity.Location;

@RelationshipProperties
public class ConnectConnection {
    @Id @GeneratedValue
    private String id;

    @TargetNode
    private final Location destination;
    
    private Double distance;
    
    public ConnectConnection(Location destination, Double distance) {
        this.destination = destination;
        this.distance = distance;
    }
    
    public Location getDestination() {
        return destination;
    }
    
    public Double getDistance() {
        return distance;
    }

    public String getId() {
        return id;
    }
}
