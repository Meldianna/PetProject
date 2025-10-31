package com.fotos.redsocial.entity;


import org.springframework.data.neo4j.core.schema.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private LocalDate date;

    // Event -> Location (TAKES_PLACE_IN)
    @Relationship(type = "TAKES_PLACE_IN", direction = Relationship.Direction.OUTGOING)
    private Location location;

    // Event -> User (ORGANIZED_BY) 
    @Relationship(type = "ORGANIZED_BY", direction = Relationship.Direction.OUTGOING)
    private User organizer;

    // (:User)-[:ENROLLED_IN]->(:Event)  => Usuarios del evento
    @Relationship(type = "ENROLLED_IN", direction = Relationship.Direction.INCOMING)
    private List<User> enrolledUsers;

    // Shelters que participan del evento (Shelter -> PARTICIPATES_IN -> Event)
    @Relationship(type = "PARTICIPATES_IN", direction = Relationship.Direction.INCOMING)
    private List<Shelter> participatingShelters;
}
