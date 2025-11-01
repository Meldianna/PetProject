package com.fotos.redsocial.service.interfaces;

import com.fotos.redsocial.entity.dto.requests.LocationRequest;
import com.fotos.redsocial.entity.dto.responses.LocationResponse;


public interface LocationService {
    LocationResponse createLocation(LocationRequest request);
    
}
