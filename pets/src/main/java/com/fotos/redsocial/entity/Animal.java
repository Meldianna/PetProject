package com.fotos.redsocial.entity;



import java.util.ArrayList;
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
public class Animal {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int age;
    private String gender;
    private Status status; // por ejemplo: available o adopted.
    //cuando está available, puede ser ADOPTADO, APADRINADO o EN TRÁNSITO
    //cuando está adoptado, no se puede hacer ninguna de esas acciones

    // Animal -> Species (IS_A)
    @Relationship(type = "IS_A", direction = Relationship.Direction.OUTGOING)
    private Specie specie;

    // Shelter -> HOUSES -> Animal
    @Relationship(type = "HOUSES", direction = Relationship.Direction.INCOMING)
    private Shelter shelter;

    // Users que cuidan o adoptaron al animal: (:User)-[:TAKE_CARE_OF|:ADOPTS|:FOSTERS]->(:Animal)
    @Relationship(type = "TAKE_CARE_OF", direction = Relationship.Direction.INCOMING)
    private List<User> caretakers;

    @Relationship(type = "ADOPTS", direction = Relationship.Direction.INCOMING)
    private User adopter;

    //personas que dan tránsito
    @Relationship(type = "FOSTERS", direction = Relationship.Direction.INCOMING)
    private User fosterer;

    /*Pensé en hacer todas estas distenciones porque
     * 1- en la vida real, se suele registrar/asociar al animal adoptado a una persona
     * 2- siguiendo la lógica, un animal puede ser apadrinado por varias personas
     * 3- siguiendo la lógica, un animal está en tránsito en la casa u hogar de una persona, pero puede tener muchos padrinos.
     */
     @Relationship(type = "HAS_TRAIT", direction = Relationship.Direction.OUTGOING)
     private List<Trait> traits;

     //by default, the animal has these principal characteristics.
     //traits and users are set later
     public Animal(String name, int age, String gender, Specie specie, Shelter shelter){
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.specie = specie;
            this.shelter = shelter;
            this.status = Status.AVAILABLE; //becomes available by default
            
            //initating the data structures
            this.caretakers = new ArrayList<>();
            this.traits = new ArrayList<>();

            //initating the users
            this.adopter = null;
            this.fosterer = null;
     }


}
