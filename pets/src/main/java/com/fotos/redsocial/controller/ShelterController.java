package com.fotos.redsocial.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fotos.redsocial.entity.dto.requests.ShelterRequest;
import com.fotos.redsocial.service.ShelterServiceImpl;

@RequestMapping("/pets/shelter")
@RestController
public class ShelterController {
    @Autowired
    private ShelterServiceImpl shelterService;

    public ShelterController(){}

    @PostMapping("/create")
    public ResponseEntity<Object> createShelter(@RequestBody ShelterRequest request) {
        try {
            shelterService.createShelter(request);
            return ResponseEntity.ok().body("Shelter creado correctamente");
        }catch (Exception error){
            throw new RuntimeException(error.getMessage());
        }
    }
    
    
    @PostMapping("/addAnimal")
    public ResponseEntity<Object> addAnimalToShelter(@RequestBody String shelterName, Long animalId){
        try {
            shelterService.addAnimaltoShelter(shelterName, animalId);
            return ResponseEntity.ok().body("Agregado el animal al shelter correctamente");
        }catch (Exception error){
            throw new RuntimeException(error.getMessage());
        }
    }
        
}