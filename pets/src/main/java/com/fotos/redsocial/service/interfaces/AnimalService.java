package com.fotos.redsocial.service.interfaces;

import com.fotos.redsocial.entity.dto.requests.AnimalRequest;
import com.fotos.redsocial.entity.dto.responses.SimpleAnimalResponse;

public interface AnimalService {
    SimpleAnimalResponse createAnimal(AnimalRequest request);
}
