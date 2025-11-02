package com.fotos.redsocial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fotos.redsocial.service.ShelterServiceImpl;

@RestController
@RequestMapping("/pets/shelter")
public class ShelterController {
    
    @Autowired
    private ShelterServiceImpl shelterService;

    
}
