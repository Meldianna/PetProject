package com.fotos.redsocial.entity.dto.responses;

import java.util.List;

import com.fotos.redsocial.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleAnimalResponse {
    private final String name;
    private final int age;
    private final String gender;
    private final SpecieResponse specie;
    private final List<TraitResponse> traits;
    private final Status status;
}
