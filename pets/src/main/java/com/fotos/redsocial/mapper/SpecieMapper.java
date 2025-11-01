package com.fotos.redsocial.mapper;

import org.springframework.stereotype.Component;

import com.fotos.redsocial.entity.Specie;
import com.fotos.redsocial.entity.dto.responses.SpecieResponse;

@Component
public class SpecieMapper {
    
    public SpecieResponse tospSpecieResponse(Specie specie){
        return new SpecieResponse(
            specie.getName()
        );
    }
}
