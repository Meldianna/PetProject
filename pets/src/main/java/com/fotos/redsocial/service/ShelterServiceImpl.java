package com.fotos.redsocial.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.Animal;
import com.fotos.redsocial.entity.Location;
import com.fotos.redsocial.entity.Shelter;
import com.fotos.redsocial.entity.dto.requests.ShelterRequest;
import com.fotos.redsocial.entity.dto.responses.ShelterResponse;
import com.fotos.redsocial.entity.dto.responses.SimpleAnimalResponse;
import com.fotos.redsocial.entity.relationship.HousesRelationship;
import com.fotos.redsocial.mapper.AnimalMapper;
import com.fotos.redsocial.mapper.ShelterMapper;
import com.fotos.redsocial.repository.AnimalRepository;
import com.fotos.redsocial.repository.LocationRepository;
import com.fotos.redsocial.repository.ShelterRepository;

@Service
public class ShelterServiceImpl {

    @Autowired
    private ShelterRepository shelterRepository;
    @Autowired
    private ShelterMapper shelterMapper;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private AnimalMapper animalMapper;

    public ShelterResponse createShelter(ShelterRequest request) {
        
        Shelter existingShelter = shelterRepository.findByName(request.getName());
        if (existingShelter != null) throw new RuntimeException("Shelter Repetido");

        Location existingLocation = locationRepository.findByName(request.getLocation());
        if (existingLocation == null) throw new RuntimeException("Localidad inexistente");

        Shelter newShelter = new Shelter(
            request.getName(),
            request.getAddress(),
            existingLocation
        );
        shelterRepository.save(newShelter);
        return shelterMapper.toShelterResponse(newShelter);
    }

    public SimpleAnimalResponse addAnimaltoShelter(String shelterName, String animalId) {

        Shelter shelter = shelterRepository.findByName(shelterName);
        if (shelter == null) throw new RuntimeException("Shelter no encontrado");

        Optional<Animal> animal = animalRepository.findById(animalId);
        if (animal.isEmpty()) throw new RuntimeException("Animal no encontrado");

        shelter.getHouses().add(new HousesRelationship(LocalDate.now(), animal.get()));
        shelterRepository.save(shelter);

        return animalMapper.toSimpleAnimalResponse(animal.get());

    }
}
