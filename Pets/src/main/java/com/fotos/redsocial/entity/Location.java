package com.fotos.redsocial.entity;



import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.fotos.redsocial.entity.relationship.ConnectConnection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Node("Location")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location{

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private String name;
    private String address;
    
    @Relationship(type = "CONNECTS")
    private List<ConnectConnection> connections = new ArrayList<>();
    
    @Relationship(type = "LOCATED_AT", direction = Relationship.Direction.INCOMING)
    private List<Shelter> shelters = new ArrayList<>();

    public Location (String name){
        this.name = name;
        this.connections = new ArrayList<>();
        this.shelters = new ArrayList<>();

    }
    
    public List<Shelter> getShelters() {
        return shelters != null ? shelters : new ArrayList<>();
    }

    public List<ConnectConnection> getConnections() {
        return connections != null ? connections : new ArrayList<>();
    }

    public String getId() {
        return id;
    }
}
