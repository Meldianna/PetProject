package com.fotos.redsocial.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fotos.redsocial.entity.Animal;
import com.fotos.redsocial.entity.dto.responses.SimpleAnimalResponse;
import com.fotos.redsocial.entity.dto.responses.SpecieResponse;
import com.fotos.redsocial.entity.dto.responses.TraitResponse;

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
    
}
