package com.fotos.redsocial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.Specie;
import com.fotos.redsocial.entity.dto.requests.SpecieRequest;
import com.fotos.redsocial.entity.dto.responses.SpecieResponse;
import com.fotos.redsocial.mapper.SpecieMapper;
import com.fotos.redsocial.repository.SpecieRepository;
import com.fotos.redsocial.service.interfaces.SpecieService;

@Service
public class SpecieServiceImpl implements SpecieService{

    @Autowired
    private SpecieRepository specieRepository;

    @Autowired
    private SpecieMapper specieMapper;

    @Override
    public SpecieResponse createSpecie(SpecieRequest request) {
        Specie existingSpecie = specieRepository.findByName(request.getSpecieName());

        if (existingSpecie != null) throw new RuntimeException("Tipo de animal repetido. Use uno de los existentes");

        Specie newSpecie = new Specie(
            request.getSpecieName()
        );

        specieRepository.save(newSpecie);

        return specieMapper.tospSpecieResponse(newSpecie);
    }
    
}
