package com.fotos.redsocial.service.interfaces;

import com.fotos.redsocial.entity.dto.requests.TraitRequest;
import com.fotos.redsocial.entity.dto.responses.TraitResponse;

public interface TraitService {
    TraitResponse createTrait(TraitRequest request);
}
