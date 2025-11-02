package com.fotos.redsocial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fotos.redsocial.entity.dto.requests.TraitRequest;
import com.fotos.redsocial.entity.dto.responses.TraitResponse;
import com.fotos.redsocial.service.TraitServiceImpl;


@RestController
@RequestMapping("/pets/traits")
public class TraitController {

    @Autowired
    private TraitServiceImpl traitService;
    
    public TraitController(){}

    @PostMapping("/create")
    public  ResponseEntity<Object> createTrait(@RequestBody TraitRequest request) {
         try{TraitResponse response = traitService.createTrait(request);
        return ResponseEntity.ok().body(response);
        }catch(Exception error){
        throw new RuntimeException(error.getMessage());
        }
    }
    
}
