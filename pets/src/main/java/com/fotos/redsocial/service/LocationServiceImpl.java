package com.fotos.redsocial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.Location;
import com.fotos.redsocial.entity.dto.requests.LocationRequest;
import com.fotos.redsocial.entity.dto.responses.LocationResponse;
import com.fotos.redsocial.mapper.LocationMapper;
import com.fotos.redsocial.repository.LocationRepository;
import com.fotos.redsocial.service.interfaces.LocationService;

@Service
public class LocationServiceImpl implements LocationService{
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    @Override
    public LocationResponse createLocation(LocationRequest request) {
        
        Location existingLocation = locationRepository.findByName(request.getLocationName());

        if (existingLocation != null) throw new RuntimeException("Localidad Repetida");

        Location newLocation = new Location(
            request.getLocationName(),
            request.getStateName()
        );

        locationRepository.save(newLocation);
        
        return locationMapper.tLocationResponse(newLocation);
    }
    
}
