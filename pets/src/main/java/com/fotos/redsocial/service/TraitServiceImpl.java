package com.fotos.redsocial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.Trait;
import com.fotos.redsocial.entity.dto.requests.TraitRequest;
import com.fotos.redsocial.entity.dto.responses.TraitResponse;
import com.fotos.redsocial.mapper.TraitMapper;
import com.fotos.redsocial.repository.TraitRepository;
import com.fotos.redsocial.service.interfaces.TraitService;

@Service
public class TraitServiceImpl implements TraitService{

    @Autowired
    private TraitRepository traitRepository;

    @Autowired
    private TraitMapper traitMapper;


    @Override
    public TraitResponse createTrait(TraitRequest request) {
        Trait existingTrait = traitRepository.findByDescription(request.getDescription());
        if (existingTrait != null) throw new RuntimeException("Característica repetida");

        Trait newTrait = new Trait(
            request.getDescription()
        );

        traitRepository.save(newTrait);

        return traitMapper.toTraitResponse(newTrait);

    }
    
}
