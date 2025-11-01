package com.fotos.redsocial.entity.dto.responses;

import java.util.List;

import com.fotos.redsocial.entity.Location;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShelterResponse {

    private String name;
    private String address;
    private Location locatedIn;
    private List<SimpleAnimalResponse> rescuedAnimals;
    private List<SimpleUserResponse> followers;

    //could be useful when detailing the shelter info
    //private List<SimpleUserDTO> administers;
    //private List<SimpleUserDTO> workers;


    
}
