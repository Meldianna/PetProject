package com.fotos.redsocial.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fotos.redsocial.entity.Animal;
import com.fotos.redsocial.entity.dto.responses.SimpleAnimalResponse;
import com.fotos.redsocial.entity.dto.responses.SpecieResponse;
import com.fotos.redsocial.entity.dto.responses.TraitResponse;
import com.fotos.redsocial.entity.relationship.HousesRelationship;

@Component
public class AnimalMapper {

    @Autowired
    private SpecieMapper specieMapper;

    @Autowired
    private TraitMapper traitMapper;

    public SimpleAnimalResponse toSimpleAnimalResponse(Animal animal){
        
        SpecieResponse specie = specieMapper.tospSpecieResponse(animal.getSpecie());
        List<TraitResponse> traitList = traitMapper.toTraitResponseList(animal.getTraits());

        return new SimpleAnimalResponse(
            animal.getName(),
            animal.getAge(),
            animal.getGender(),
            specie,
            traitList,
            animal.getStatus()
        );
    }

    public List<SimpleAnimalResponse> toSimpleAnimalResponseList(List<Animal> list){
        return list.stream()
        .map(this::toSimpleAnimalResponse)
        .collect(Collectors.toList());
    }

    //para obtener los animales de la relación HOUSES 
    public List<SimpleAnimalResponse> fromHousesRelationshipList(List<HousesRelationship> relationships) {
       
        return relationships.stream()
            //Otenemos el animal de la relación
            .map(HousesRelationship::getAnimal) 
            //Lo mapeamos a su DTO
            .map(this::toSimpleAnimalResponse) 
            .collect(Collectors.toList());
    }
    
}
