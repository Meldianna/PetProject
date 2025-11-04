package com.fotos.redsocial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.Animal;
import com.fotos.redsocial.entity.Shelter;
import com.fotos.redsocial.entity.Specie;
import com.fotos.redsocial.entity.Trait;
import com.fotos.redsocial.entity.dto.requests.AnimalRequest;
import com.fotos.redsocial.entity.dto.responses.SimpleAnimalResponse;
import com.fotos.redsocial.mapper.AnimalMapper;
import com.fotos.redsocial.repository.AnimalRepository;
import com.fotos.redsocial.repository.ShelterRepository;
import com.fotos.redsocial.repository.SpecieRepository;
import com.fotos.redsocial.repository.TraitRepository;
import com.fotos.redsocial.service.interfaces.AnimalService;


@Service
public class AnimalServiceImpl implements AnimalService{

    //repositories
    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private SpecieRepository specieRepository;

    @Autowired
    private TraitRepository traitRepository;

    //mappers
    @Autowired
    private AnimalMapper animalMapper;


    @Override
    public SimpleAnimalResponse createAnimal(AnimalRequest request) {
        
        Shelter shelter = shelterRepository.findByName(request.getShelterName());
        if (shelter == null) throw new RuntimeException("Shelter no encontrado");
        Specie specie = specieRepository.findByName(request.getSpecie());
        if (specie == null) throw new RuntimeException("Especie no encontrada");

        //consiguiendo los rasgos
        List<String> traitNames = request.getTraits();

        //buscando los rasgos
        List<Trait> traitList = traitRepository.findAllByDescriptionIn(traitNames);
        //creando al animal
        Animal newAnimal = new Animal(
            request.getName(),
            request.getAge(),
            request.getGender(),
            specie,
            shelter
        );
        
        newAnimal.setTraits(traitList);
        
        //guardando el animal
        Animal savedAnimal = animalRepository.save(newAnimal);

        return animalMapper.toSimpleAnimalResponse(savedAnimal);
    }
}
