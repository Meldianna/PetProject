package com.fotos.redsocial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fotos.redsocial.entity.dto.requests.SpecieRequest;
import com.fotos.redsocial.entity.dto.requests.TraitRequest;
import com.fotos.redsocial.entity.dto.responses.SpecieResponse;
import com.fotos.redsocial.entity.dto.responses.TraitResponse;
import com.fotos.redsocial.service.SpecieServiceImpl;

@RestController
@RequestMapping("/pets/species")
public class SpecieController {
    @Autowired
    private SpecieServiceImpl specieService;

    public SpecieController(){}

    @PostMapping("/create")
    public  ResponseEntity<Object> createSpecie(@RequestBody SpecieRequest request) {
         try{SpecieResponse response = specieService.createSpecie(request);
        return ResponseEntity.ok().body(response);
        }catch(Exception error){
        throw new RuntimeException(error.getMessage());
        }
    }
}
