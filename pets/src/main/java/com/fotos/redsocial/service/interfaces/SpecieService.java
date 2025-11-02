package com.fotos.redsocial.service.interfaces;

import com.fotos.redsocial.entity.dto.requests.SpecieRequest;
import com.fotos.redsocial.entity.dto.responses.SpecieResponse;


public interface SpecieService {
    SpecieResponse createSpecie(SpecieRequest request);
}
