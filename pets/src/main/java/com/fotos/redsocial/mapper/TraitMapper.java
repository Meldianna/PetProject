package com.fotos.redsocial.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fotos.redsocial.entity.Trait;
import com.fotos.redsocial.entity.dto.responses.TraitResponse;

@Component
//used to map entities to TraitResponse
public class TraitMapper {
    
    public TraitResponse toTraitResponse(Trait trait){
        if (trait == null) return null;

        return new TraitResponse(
            trait.getDescription()
        );
    }

    public List<TraitResponse> toTraitResponseList(List<Trait> list){
        return list.stream()
        .map(this::toTraitResponse)
        .collect(Collectors.toList());
    }
}
