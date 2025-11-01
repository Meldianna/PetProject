package com.fotos.redsocial.entity.dto.responses;

import java.util.List;

import com.fotos.redsocial.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailedAnimalResponse {
    private String name;
    private int age;
    private String gender;
    private Status status;
    private SpecieResponse specie;
    private List<TraitResponse> traits;

    private List<SimpleUserResponse> caretakers;
    private SimpleUserResponse adopter;
    private SimpleUserResponse fosterer;
}
