package com.fotos.redsocial.entity;



import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;
import com.fotos.redsocial.entity.relationship.RoadConnection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location{

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private String name;
    private String state;

    // Relación ROAD_TO con propiedad 'distance'
    @Relationship(type = "ROAD_TO", direction = Relationship.Direction.OUTGOING)
    private List<RoadConnection> roadTo;

    // Eventos que toman lugar en esta ubicación (entrada desde Event)
    @Relationship(type = "TAKES_PLACE_IN", direction = Relationship.Direction.INCOMING)
    private List<Event> eventsHere;

    public String getState(){
        return this.state;
    }

    public Location (String name, String state){
        this.name = name;
        this.state = state;
        this.roadTo = new ArrayList<>();
        this.eventsHere = new ArrayList<>();

    }
}
