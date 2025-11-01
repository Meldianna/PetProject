package com.fotos.redsocial.mapper;


import org.springframework.stereotype.Component;

import com.fotos.redsocial.entity.Location;
import com.fotos.redsocial.entity.dto.responses.LocationResponse;

@Component
public class LocationMapper {
    
    public LocationResponse tLocationResponse(Location location){
        return new LocationResponse(
            location.getName(), 
            location.getState()
            );
    }
}
