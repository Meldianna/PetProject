package com.fotos.redsocial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fotos.redsocial.entity.dto.requests.LocationRequest;
import com.fotos.redsocial.entity.dto.responses.LocationResponse;
import com.fotos.redsocial.service.LocationServiceImpl;


@RestController
@RequestMapping("/pets/location")
public class LocationController {
    
    @Autowired
    private LocationServiceImpl locationService;

    @PostMapping("/add")
    public ResponseEntity<Object> createLocation(@RequestBody LocationRequest request) {
        try{LocationResponse response= locationService.createLocation(request);
        return ResponseEntity.ok().body(response);
        }catch(Exception error){
        throw new RuntimeException(error.getMessage());
        }
    }
}
