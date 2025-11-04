package com.fotos.redsocial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fotos.redsocial.entity.dto.requests.AnimalRequest;
import com.fotos.redsocial.entity.dto.responses.SimpleAnimalResponse;
import com.fotos.redsocial.service.AnimalServiceImpl;




@RestController
@RequestMapping("/pets/animal")
public class AnimalController {
    
    @Autowired
    private AnimalServiceImpl animalService;
    
    public AnimalController(){}

    @PostMapping("/add")
    public ResponseEntity<Object> createAnimal(@RequestBody AnimalRequest request) {
        try{SimpleAnimalResponse response = animalService.createAnimal(request);
        return ResponseEntity.ok().body(response);
        }catch(Exception error){
        throw new RuntimeException(error.getMessage());
        }
    }
    
    
}
