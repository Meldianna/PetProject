package com.fotos.redsocial.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fotos.redsocial.entity.Shelter;
import com.fotos.redsocial.entity.dto.responses.LocationResponse;
import com.fotos.redsocial.entity.dto.responses.ShelterResponse;
import com.fotos.redsocial.entity.dto.responses.SimpleAnimalResponse;
import com.fotos.redsocial.entity.dto.responses.SimpleUserResponse;
import com.fotos.redsocial.entity.relationship.HousesRelationship;

@Component
public class ShelterMapper {
    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private AnimalMapper animalMapper;

    @Autowired
    private UserMapper userMapper;

    public ShelterResponse toShelterResponse(Shelter shelter){
        LocationResponse location = locationMapper.tLocationResponse(shelter.getLocatedIn());
        List<SimpleAnimalResponse> animals = animalMapper.fromHousesRelationshipList(shelter.getHouses());
        List<SimpleUserResponse> followers = userMapper.toSimpleUserResponseList(shelter.getFollowers());

        return new ShelterResponse(
            shelter.getName(),
            shelter.getAddress(),
            location,
            animals,
            followers
        );
    }
    
}
